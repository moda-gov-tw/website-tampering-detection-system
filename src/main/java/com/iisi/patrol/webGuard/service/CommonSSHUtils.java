package com.iisi.patrol.webGuard.service;

import com.iisi.patrol.webGuard.service.sshService.ConnectionConfig;
import com.jcraft.jsch.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class CommonSSHUtils {

    public static String useSshCommand(ConnectionConfig connectionConfig, String command) throws JSchException {

        Session session = null;
        ChannelExec channel = null;
        String responseString = null;
        String errorString = "";
        try {
            session = new JSch().getSession(connectionConfig.getUserName(), connectionConfig.getServerIp(), connectionConfig.getPort());
            session.setPassword(connectionConfig.getPassWord());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ((ChannelExec)channel).setErrStream(System.err);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.setErrStream(errorStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }
            errorString = errorStream.toString();
            responseString = responseStream.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        if(errorString.length()>0){
            return errorString.trim();
        }

        return responseString.trim();
    }

    public static void useScpCopyRemoteFile(ConnectionConfig connectionConfig,String from, String to,String fileName) throws JSchException, IOException {
        Session session = CommonSSHUtils.createSession(connectionConfig);
        copyRemoteToLocal(session,from,to,fileName);
    }

    public static void useScpCopyLocalFileToRemote(ConnectionConfig connectionConfig,String from, String to,String fileName) throws JSchException, IOException {
        Session session = CommonSSHUtils.createSession(connectionConfig);
        copyLocalToRemote(session,from,to,fileName);
    }


    private static Session createSession(ConnectionConfig connectionConfig) {
        try {
            Session session = null;
            session = new JSch().getSession(connectionConfig.getUserName(),connectionConfig.getServerIp(), connectionConfig.getPort());
            session.setPassword(connectionConfig.getPassWord());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return session;
        } catch (JSchException e) {
            System.out.println(e);
            return null;
        }
    }

    private static void copyRemoteToLocal(Session session, String from, String to, String fileName) throws JSchException, IOException {
        // 校驗 fileName 以避免目錄穿越攻擊
        if (!isValidFileName(fileName)) {
            throw new IllegalArgumentException("非法文件名");
        }

        // 校驗 from 和 to 路徑是否是合法路徑
        if (!isValidPath(from) || !isValidPath(to)) {
            throw new IllegalArgumentException("非法路径");
        }

        from = from + File.separator + fileName;
        String prefix = null;

        if (new File(to).isDirectory()) {
            prefix = to + File.separator;
        }

        // exec 'scp -f rfile' remotely
        String command = "scp -f " + from;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] buf = new byte[8096];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while (true) {
            int c = checkAck(in);
            if (c != 'C') {
                break;
            }

            // read '0644 '
            //in.read(buf, 0, 5);下面的寫法比較不會有read的bug
            int bytes_read = 0;
            while (bytes_read < 5){
                bytes_read += in.read(buf, 0, 5 - bytes_read);
            }

            long filesize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ') break;
                filesize = filesize * 10L + (long) (buf[0] - '0');
            }

            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }

            //System.out.println("file-size=" + filesize + ", file=" + file);

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            // read a content of lfile
            FileOutputStream fos = new FileOutputStream(prefix == null ? to : prefix + file);
            int foo;
            while (true) {
                if (buf.length < filesize) foo = buf.length;
                else foo = (int) filesize;
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                fos.write(buf, 0, foo);
                filesize -= foo;
                if (filesize == 0L) break;
            }

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            try {
                if (fos != null) fos.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        channel.disconnect();
        session.disconnect();

    }

    // 校驗檔案名，確保檔案名不包含危險字元
    private static boolean isValidFileName(String fileName) {
        return fileName != null && fileName.matches("^[\\w,\\s-]+\\.[A-Za-z]{3}$"); // 示例正则表达式，根据需求调整
    }

    // 校驗路徑，防止路徑跳躍攻擊
    private static boolean isValidPath(String path) throws IOException {
        return path != null && !path.contains("..") && new File(path).getCanonicalFile().equals(new File(path).getAbsolutePath());
    }

    // 轉義 Shell 特殊字元，防止命令注入
    private static String escapeShellArgument(String arg) {
        return arg.replace("'", "'\"'\"'"); // 简单的转义方法，可以根据实际环境进行扩展
    }

    public static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    /**
     *
     * @param session 連線資訊
     * @param from  本機檔案位置(不含檔名)
     * @param to    ssh檔案位置(不含檔名)
     * @param fileName 檔名
     * @throws JSchException
     * @throws IOException
     */
    private static void copyLocalToRemote(Session session, String from, String to, String fileName) throws JSchException, IOException {
        boolean ptimestamp = true;
        from = from + File.separator + fileName;
        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + to;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        File _lfile = new File(from);

        if (ptimestamp) {
            command = "T" + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (from.lastIndexOf('/') > 0) {
            command += from.substring(from.lastIndexOf('/') + 1);
        }else if(from.lastIndexOf('\\') > 0){
            command += from.substring(from.lastIndexOf('\\') + 1);
        }
        else {
            command += from;
        }
        FileInputStream fis = new FileInputStream(from);
        command += "\n";
        out.write(command.getBytes());
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

//        // send a content of lfile
//        FileInputStream fis = new FileInputStream(from);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
        }

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        try {
            if (fis != null) fis.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        channel.disconnect();
        session.disconnect();
    }


}

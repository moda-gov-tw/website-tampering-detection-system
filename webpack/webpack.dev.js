"use strict";
const BrowserSyncPlugin = require("browser-sync-webpack-plugin");
const { VueLoaderPlugin } = require("vue-loader");

module.exports = (env, options) => {
  const devConfig = {
    //配置vue css scss loader
    module: {
      rules: [
        {
          test: /\.vue$/,
          use: ["vue-loader"],
          //vue-loader用途:以處理 .vue檔，vue-loader 會將每一個語言塊提取出來並送至相關的 loader 去做處理，比如說 <style>、<templete> 等
        },
        //處理scss
        {
          test: /\.s[ac]ss$/i,
          use: ["style-loader", "css-loader", "postcss-loader", "sass-loader"], 
          //style-loader用途:將css-loader 處理過後的 CSS 注入到 HTML 內，將以 style 標籤的形式存在
          //postcssloader用途:將css後處理，詳細的配置寫在postcss.config
        },
        //處理css的loader
        {
          test: /\.css$/,
          use: ["style-loader", "css-loader", "postcss-loader"],
        },
      ],
    },
    //source map可以提供完整的bundle後的source code資訊
    devtool: "source-map", 
    //用name來當bundle後的moduleId,方便開發
    optimization: {
      moduleIds: "named",
    },
    //用BrowserSyncPlugin 自動refresh和同步各個瀏覽器
    plugins: [
      new VueLoaderPlugin(),
      new BrowserSyncPlugin(
        {
          host: "localhost",
          port: 9000,
          proxy: {
            target: `http://localhost:9060`,
            ws: true,
          },
          socket: {
            clients: {
              heartbeatTimeout: 60000,
            },
          },
        },
        {
          reload: true,
        }
      ),
    ],
  };
  return devConfig;
};

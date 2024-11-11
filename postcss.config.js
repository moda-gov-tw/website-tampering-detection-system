//定義要用什麼post css configuration 
// Learn more about it at https://github.com/webpack-contrib/postcss-loader#config-files

/*
  autoprefixer 幫CSS檔案「自動」加上各種瀏覽器的前綴
  ex: transition: all .5s;
  轉成  -webkit-transition: all .5s;     
        -o-transition: all .5s;     
        transition: all .5s; 
*/

module.exports = {
  plugins: [["autoprefixer"]],
};

// webpack.config.js
const path = require("path");
const { merge } = require("webpack-merge");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const webpack = require("webpack");

//path.resolve是node js提供的,webpack是用path.join,所以包裝起來
function resolve(dir = "") {
  return path.join(__dirname, "..", dir);
}
//用webpack merge 切分開發環境與建置環境
module.exports = async (env, options) => {
  console.log("env:", env);
  console.log("options:", options);
  const development = options.mode === "development"; //判斷為開發模式dev或是打包正式環境prod
  const webpackConfig = await merge(
    {
      mode: options.mode, //從package.json給的,//mode 有三種 development,production,none
      entry: path.resolve("./src/main/webapp/app/main.ts"), // 入口
      output: {
        path: resolve("target/classes/static/"), //output
        //filename: 'js/[name].js' //out put file name
      },
      //需要解析的檔案
      resolve: {
        extensions: [".ts", ".js", ".vue", ".json"],
        alias: {
          //vue$: 'vue/dist/vue.esm.js', //vue2用的，如果vue2不加這個，遇到import vue似乎會有問題,但用vue3加了會有問題
          "@": resolve("src/main/webapp/app"), //方便寫絕對路徑
        },
      },
      devServer: {
        static: {
          directory: "./target/classes/static/",
        },
        port: 9060,
        proxy: {
          "/api": {
            target: "http://localhost:8088",            
          },
        },
        // [
        //   {
        //     context: [],
        //     target: "http://localhost:8080",
        //     secure: false,
        //   },
        // ],
        historyApiFallback: true, //處理refresh的時候會去打後端的問題
        //   historyApiFallback: {
        //   },
      },
      module: {
        rules: [
          //這邊只有配置ts和js的
          //處理ts的loader
          {
            test: /\.ts(x)?$/,
            loader: "ts-loader",
            exclude: /node_modules/,
            //在vue裡面 lang =ts 也要處理
            options: {
              appendTsSuffixTo: [/\.vue$/],
            },
          },
          //這幾個結尾都當作資源檔(need study)
          {
            test: /\.(png|jpe?g|gif|svg|mp4|webm|ogg|mp3|wav|flac|aac|woff2?|eot|ttf|otf)/,
            type: "asset/resource",
          },
        ],
      },
      plugins: [
        // new VueLoaderPlugin(),
        new HtmlWebpackPlugin({
          template: path.resolve(__dirname, "../src/main/webapp/index.html"), // html template location
          filename: "index.html", // out put file name
        }),
        new CopyWebpackPlugin({
          patterns: [
            //把webapp content底下的東西搬到target/content
            // { from: './src/main/webapp/content/', to: 'content/' },
            { from: "./src/main/webapp/favicon.ico", to: "favicon.ico" },
          ],
        }),
        /**
         * 處理以下waring
         * ature flags __VUE_OPTIONS_API__, __VUE_PROD_DEVTOOLS__ are not explicitly defined.
         * You are running the esm-bundler build of Vue, which expects these compile-time feature flags to be globally injected
         * via the bundler config in order to get better tree-shaking in the production bundle.
         */
        new webpack.DefinePlugin({
          __VUE_OPTIONS_API__: false,
          __VUE_PROD_DEVTOOLS__: false,
        }),
      ],
    },
    await require(`./webpack.${development ? "dev" : "prod"}.js`)(env, options)
    // devConfig
  );
  return webpackConfig;
};

/** 
 * 拆profile前 的測試
module.exports = {
  mode: 'development', //開發模式的打包
  entry: path.resolve('./src/main/webapp/app/main.ts'), // 入口
  output: {
    path: resolve('target/classes/static/'), //output
    filename: 'js/[name].js' //out put file name
  },
  devServer: {
    static: {
        directory: './target/classes/static/',
      },
      port: 9060,
      proxy: [
        {
          context: [],
          target: 'http://localhost:8080',
          secure: false,
        },
      ],
    //   historyApiFallback: {
    //   },
  },
  resolve: {
    extensions: [
      '.js',
      '.vue',
      '.tsx',
      '.ts'
    ]
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        use: [
          'vue-loader'
        ]
      },
      //處理scss
      {
        test: /\.s[ac]ss$/i,
        use: ['style-loader', "css-loader", "postcss-loader", "sass-loader"],
      },
      //處理css的loader
      {
        test: /\.css$/,
          use: ['style-loader','css-loader',"postcss-loader"]
      },
      //處理ts的loader
      {
        test: /\.ts(x)?$/,
        loader: 'ts-loader',
        exclude: /node_modules/,
        //在vue裡面也要處理
        options: {
          appendTsSuffixTo: [
            /\.vue$/
          ]
        }
      },
      //把es6轉成瀏覽器看得懂的js,但如果用ts開發那基本附檔名不會有.js的,所以應該用不到
      {
        test: /\.js$/,
        exclude: /node_modules/, //
        loader: 'babel-loader',
        //babel的詳細設定，可寫在babel.config.js 檔案或是寫在下面
        options: {
            presets: [
              [
                '@babel/preset-env',
                {
                  targets: {
                    "browsers": ["last 2 versions"] // 最近兩個版本
                  },
                  useBuiltIns: 'entry',
                  corejs: 3,
                },
              ],
            ],
        },
      },
      
    ]
  },
  plugins:[
    new HtmlWebpackPlugin({
        template: path.resolve(__dirname, '../src/main/webapp/index.html'), // html template location
        filename: 'index.html', // out put file name
    }),
    new VueLoaderPlugin()
  ]
}
*/

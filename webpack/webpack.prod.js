
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');
const { VueLoaderPlugin } = require("vue-loader");

module.exports = (env, options) => {
    const prodConfig = {
      //配置vue css scss loader
      module: {
        rules: [
          {
            test: /\.vue$/,
            use: ["vue-loader"],
          },
          //處理scss
          {
            test: /\.s[ac]ss$/i,
            use: [MiniCssExtractPlugin.loader, "css-loader", "postcss-loader", "sass-loader"],
          },
          //處理css的loader
          {
            test: /\.css$/,
            use: [MiniCssExtractPlugin.loader, "css-loader", "postcss-loader"],
          },
        ],
      },
      devtool: false,
      optimization: {
        moduleIds: 'deterministic',
        minimizer: [
          new CssMinimizerPlugin(),
        ],       
      },
      plugins: [
        new MiniCssExtractPlugin(),
        new VueLoaderPlugin(),
      ],
    };
    return prodConfig;
  };
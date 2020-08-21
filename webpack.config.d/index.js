const HtmlWebpackPlugin = require('html-webpack-plugin')
config.plugins.push(
    new HtmlWebpackPlugin({
        templateContent: `
            <html>
              <body>
                <div id="app-root"></div>
              </body>
            </html>
         `
    })
)
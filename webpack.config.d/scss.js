config.resolve.modules.push("processedResources/js/main/");

config.module.rules.push({
    test: /\.s[ac]ss$/,
    use: ['style-loader', 'css-loader', 'sass-loader']
});
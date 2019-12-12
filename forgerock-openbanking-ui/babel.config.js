module.exports = function(api) {
  api.cache(true);

  return {
    plugins: ['transform-es2015-modules-commonjs'],
    presets: [
      [
        '@babel/preset-env',
        {
          targets: {
            node: 'current'
          }
        }
      ]
    ]
  };
};

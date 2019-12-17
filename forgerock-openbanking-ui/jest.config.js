const esModules = ['lodash-es'].join('|');

module.exports = {
  globals: {
    'ts-jest': {
      allowSyntheticDefaultImports: true,
      isolatedModules: true
    }
  },
  transformIgnorePatterns: [`node_modules/(?!${esModules})`],
  transform: { '^.+\\.js$': 'babel-jest' },
  setupFiles: ['<rootDir>/tests/mocks/matchMedia.js'],
  moduleNameMapper: {
    '@tests/(.*)': '<rootDir>/tests/$1',
    '@utils/(.*)': '<rootDir>/utils/$1',
    '@fuse/(.*)': '<rootDir>/projects/@fuse/$1',
    'analytics/(.*)': '<rootDir>/projects/analytics/$1',
    'bank/(.*)': '<rootDir>/projects/bank/$1',
    'auth/(.*)': '<rootDir>/projects/auth/$1',
    'directory/(.*)': '<rootDir>/projects/directory/$1',
    'manual-onboarding/(.*)': '<rootDir>/projects/manual-onboarding/$1',
    '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
      '<rootDir>/tests/mocks/fileMock.js',
    '\\.(css|scss|less)$': '<rootDir>/tests/mocks/styleMock.js'
  }
};

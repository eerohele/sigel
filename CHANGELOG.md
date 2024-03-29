# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## 1.1.0 (2024-01-17)
- **BREAKING**: Disallow XXE by default #8

  While Sigel does not normally make breaking changes; security issues are an exception. Clojure [sets a precedent](https://github.com/clojure/clojure/commit/4a4a6e7717d411679820c4a3ce735a77aef45cc3) for the same issue.

## 1.0.3 (2023-04-27)
- Extend `XMLSource` to `InputStream`

## 1.0.2 (2023-02-23)
- Fix `xpath/value-of` NPE
- Bump deps

## 1.0.1 (2020-09-15)
- Fix support for transformation pipelines that yield multiple root node children #4

## 1.0.0 (2020-02-19)
- Update to Saxon v9.9.1-6

## 0.2.3 (2019-06-26)
- Fix support for non-Latin characters in sexp stylesheets #3

## 0.2.2 (2019-01-30)
- Improve support for loading a stylesheet from the classpath #2

## 0.2.1 (2019-01-19)
- Fix XSLT 1.0 compatibility #1

## 0.2.0 (2017-11-28)
- Add `compile-xslt` function

## 0.1.0 (2017-09-19)
- Initial release

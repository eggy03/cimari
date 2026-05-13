# Changelog

All notable changes to this project will be documented in this file.

Please check out the [Releases](https://github.com/eggy03/cimari/releases) page to know more about the
commits and PRs that contributed to each of the releases.

This project tries its best to adhere to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

The following headings may be used while categorizing the list of changes made in each version:

- New Features
- Removed Features
- Bug Fixes
- Non-Breaking Changes
- Breaking Changes
- Test Changes
- Dependency Updates
- Documentation
- Known Issues

## [0.2.0] - May 13, 2026

### New Features

- Add experimental native image support via generated reachability metadata in an attempt to make cimari run out of the
  box with GraalVM native image

### Removed Features

- Removed `toJson()` methods from entities

### Non-Breaking Changes

- Build mode is now separated to two maven profiles: `dist` and `native`

## [0.1.0] - April 20, 2026

- Initial Release
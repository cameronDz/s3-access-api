# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Created validation classes for checking S3 request payloads before trying to communicate
- New custom exceptions for failed validations
- Basic System out logging on exception catches
- Ability to add new JSONs blobs to S3 and update index in bucket

### Changed
- All response now returning String/Object map, with a payload key
- Moved validations to own classes

## [0.0.1-SNAPSHOT] - 2020-01-26
### Added
- Initial project creation

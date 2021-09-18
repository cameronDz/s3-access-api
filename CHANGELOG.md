# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.1] 2021-09-17

### Fixed

- Authorized configuration check that was failing due converting header to lowercase

## [1.2.0] 2021-09-10

### Added

- POST endpoint for fetching list of objects from API

## [1.1.0] 2020-04-12

### Added

- Optional HTTP Authorization header for including an API key

## [1.0.1] 2020-04-12

### Added

- Ability to set feature flags for specific HTTP methods through passing optional variables

### Changed

- Set a default value (true) for public bucket flag so it no longer has to be passed

## [1.0.0] 2020-04-11

### Added

- Created validation classes for checking S3 request payloads before trying to communicate
- New custom exceptions for failed validations
- Basic System out logging on exception catches
- Ability to add new JSONs blobs to S3 and update index in bucket
- Feature flags for each HTTP method

### Changed

- All response now returning String/Object map, with a payload key
- Moved validations to own classes
- Class package naming convention

### Removed

- Removed specific object (index files) within S3 being modified

## [0.0.1-SNAPSHOT] - 2020-01-26

### Added

- Initial project creation

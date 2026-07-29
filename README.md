# Teira

Personal expense tracker for Android. Log what you spend, put it in a category, and see where the
money actually went — with a wallet overview, monthly analysis and spending history.

Teira is also where I work out what a production-shaped Android stack looks like end to end:
module boundaries that hold, Room migrations instead of a wiped database, dependency injection
wired all the way through, and CI that refuses a pull request whose unit tests fail.

## Features

- Log expenses with category and colour-coded classification
- Wallet overview of current spending
- Monthly analysis of spend per category
- History of previous analyses
- Local notifications via WorkManager
- Everything persisted locally with Room — the app works offline

## Stack

| Layer | Technology |
|---|---|
| UI | [Jetpack Compose](https://developer.android.com/compose) (Material 3) |
| Local database | [Room](https://developer.android.com/training/data-storage/room), with migrations |
| Dependency injection | [Hilt](https://dagger.dev/hilt/) |
| Background work | [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) |
| Async | Coroutines / `Flow` |
| Crash reporting & analytics | Firebase Crashlytics, Firebase Analytics |
| Tests | JUnit, Mockito, [Turbine](https://github.com/cashapp/turbine), [Truth](https://truth.dev/) |

Compile SDK 34, min SDK 28, Java 17.

## Modules

```
:app ──► :ui ──► :sdk
```

| Module | Contains |
|---|---|
| `:app` | Entry point only — `TeiraApplication`, `MainActivity`, Hilt setup, Firebase config |
| `:ui` | Screens and their ViewModels (`wallet`, `expense`, `analysis`, `history`, `settings`), the scaffold, navigation, theme and the notification worker |
| `:sdk` | Data and domain: Room database, DAOs, entities, type converters, repositories, dispatchers, notification scheduling |

`:app` deliberately holds no screen logic — it assembles. The split lets the data layer be tested
without dragging Android UI into the test source set.

## CI

Two GitHub Actions workflows:

- **`pull_request_unit_test.yml`** — runs the unit tests on every pull request
- **`build_artifact_and_release_firebase.yml`** — signs a release build and ships it to Firebase App
  Distribution

Signing credentials come from repository secrets (`SIGNING_STORE_PASSWORD`, `SIGNING_KEY_ALIAS`,
`SIGNING_KEY_PASSWORD`); no keystore is committed.

## Building

The project expects a Firebase configuration and a Maps API key, neither of which is in the
repository:

1. Add your own `app/google-services.json` from a Firebase project
2. Add `MAPS_API_KEY=your_key` to `local.properties` (read by the
   [secrets-gradle-plugin](https://github.com/google/secrets-gradle-plugin))

Then:

```
./gradlew assembleDebug
```

## Current state

The app runs, but it is not under active development — the last release was in November 2023. There
are [open issues](https://github.com/TiagoDvl/Teira/issues) from people who actually installed it,
and the bugs among them are worth knowing about before you do the same:

- [#78](https://github.com/TiagoDvl/Teira/issues/78) — an expense saved without a category is
  silently dropped, with no error shown
- [#76](https://github.com/TiagoDvl/Teira/issues/76) — entering an amount with a `-` sign can leave
  the app in a state that needs a reinstall
- [#79](https://github.com/TiagoDvl/Teira/issues/79) — every entry in the wallet view renders as
  `HIGHEST`
- [#75](https://github.com/TiagoDvl/Teira/issues/75) — there is no way to record income, only
  expenses
- [#80](https://github.com/TiagoDvl/Teira/issues/80) — history opens on an empty screen instead of
  the most recent analysis

Issues and pull requests are welcome. Thanks to
[@arnaldotecadm](https://github.com/arnaldotecadm) for taking the time to report all of the above.

# core
[![CircleCI](https://circleci.com/gh/z1lc/core/tree/master.svg?style=shield)](https://circleci.com/gh/z1lc/core/tree/master)

## Description
`core` is [my](http://www.robertsanek.com) personal repository. Most of the code is related to providing data for a quantified self dashboard on [Klipfolio](https://www.klipfolio.com/). Data is [ETL](https://en.wikipedia.org/wiki/Extract,_transform,_load)'d and sent to a [PostgreSQL](https://en.wikipedia.org/wiki/PostgreSQL) database hosted on [Google Cloud SQL](https://cloud.google.com/sql/). [Hibernate](http://hibernate.org/orm/) is used as the ORM and schema generator. Everything is scheduled with [Quartz](http://www.quartz-scheduler.org/).

### Data Sources
* [Anki](https://apps.ankiweb.net/) local SQLite database
* [Goodreads API](https://www.goodreads.com/api)
* [Google Sheets API](https://developers.google.com/sheets/)
* [HERE API](https://developer.here.com/)
* [Human API](https://www.humanapi.co/developers/)
* [Kiva API](https://build.kiva.org/)
* [Last.fm API](https://www.last.fm/api)
* [LeetCode](https://leetcode.com/) scraping
* [LIFX API](https://api.developer.lifx.com/)
* [RescueTime API](https://www.rescuetime.com/developers)
* [RottenTomatoes](https://www.rottentomatoes.com/) scraping
* [Toodledo API](https://api.toodledo.com/3/index.php) & scraping
* [WakaTime API](https://wakatime.com/developers)
* Wikipedia: [Wikimedia API](https://wikimedia.org/api/rest_v1/), [DBpedia](https://wiki.dbpedia.org/) scraping, [MediaWiki API](https://www.wikidata.org/w/api.php)

## Dependencies
1. Install the [gcloud sdk](https://cloud.google.com/sdk/install).
    * Run `gcloud init`, enter your credentials into browser.
    * When prompted, select project `z1lc-qs` / `arctic-rite-143002`.
2. Set the environment variable `GOOGLE_APPLICATION_CREDENTIALS` to point to `z1lc-qs.json`. More info [here](https://cloud.google.com/docs/authentication/getting-started).
3. Install [Anki](http://ankisrs.net/), ideally a version ≥2.1.
    * Log into Anki and sync.
    * Install the [AnkiConnect](https://ankiweb.net/shared/info/2055492159) add-on.
4. To avoid having passwords and API keys stored alongside code in Git, this project uses a file called `secrets.json` which provides secrets to the application at runtime. Ensure you've provided a valid mapping for each `com.robertsanek.util.SecretType` within the `secrets.json`, and that it is located in the root directory. You can find out where this directory is for your platform by calling `com.robertsanek.util.platform.CrossPlatformUtils::getRootPathIncludingTrailingSlash`. You can refer to the `secrets.template.json` file for an example of what the real `secrets.json` should look like. 
5. If you plan on running the `ETL` command, ensure you've run the `ETL_SETUP` command once beforehand.

## How to run
Pass a command-line argument to select one of the below (documented in `Main.java`).

* **`ETL`** will run all [ETLs](https://en.wikipedia.org/wiki/Extract,_transform,_load) and then run `DQ`.
* **`DQ`** will run data quality checks.
* **`HABITICA`** will generate an html document with a summary of [Habitica](https://habitica.com/) dailies.
* **`PASSIVE_KIVA`** will generate an html document with short-duration [Kiva](https://www.kiva.org/) loans from highly-rated field partners.
* **`WIKI`** will extract basic information about popular Wikipedia articles that refer to people, outputting a csv file and images to import into Anki.
* **`ETL_SETUP`** needs to be triggered before ETLs are run.
* **`DAEMON`** will run some combination of the above commands on a specified schedule. See `Main.java` for the exact scheduling.

Example: `java -jar target/core-1.0-SNAPSHOT.jar -command etl_setup -type manual`
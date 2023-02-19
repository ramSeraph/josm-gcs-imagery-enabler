josm-gcs-imagery-enabler
========================

Disables blacklisting of user hosted imagery on Google Cloud Storage

I think the below lines summarize my thinking behind writing this
 without going into the ugliness of the whole situation.

```
A little less conversation, a little more action, please
All this aggravation ain't satisfactioning me
A little more bite, a little less bark
A little less fight and a little more spark
```
- Some wise dude :)


# Dev Setup

 This unorthodox setup was because my macosx java build broke,
 so I had to use docker based linux to make things work.
 
 And then at the same time `apt-get update` refused to work inside my docker containers.. I suspect Jio messing with my packets. 
 

## One time setup
 * Download gradle
   * run `./setup.sh`
   * gradle ends up in `opt/` folder  
 
 * Setup project
   * run `./run.sh gradle init` 
   * sets up the necessary gradlw wrapper scripts

  * Adjust for project requirements
    * add stub folder structure in `src/` folder
    * adjust `build.gradle.kts`
    * add `releases.yml` file
    * run `./run.sh ./gradlew localdist`

## Add Code, build and test

 * Run `./build.sh` to build, requires a github token at `token.txt` for some reason I don't remember
 * start JOSM
 * Add `build/localDist/list` to your plugin sources in JOSM - one time setup

# Tetris++

Calculator implementation on Java, made to practise with creation of unit-tests and continuous integration on **GitHub Actions**.

**Used technologies:**
* Kotlin (2.0.0)
* Apache Maven (3.9.9)
* JUnit (5.10.0)
* Docker

_Made by Khozhajinov Mykola, group IM-21._

## Installation

To install and run the project on your machine, you should clone the repository to your local machine:

`git clone https://github.com/future-stardust/im-2x-lab-4-kol-oss.git`

Then, you can build the container from the Dockerfile. To do this, you have to have the Docker installed ([guide](https://docs.docker.com/engine/install/)). To build container, run the command:

`docker build -t tetris .`

Building process for first may take some long time, don't worry about it.

## Usage

After creating docker image on your machine, you can create container and enter it via next command:

`docker run -it tetris`

Now you are able to run different modes of the program via built-in scripts:

`./app.sh [MODE] [ARGS]`

There are 3 allowed modes:
* `test` - run tests only
* `file` - run tetris with file input and output
    * **Argument 1 (OPTIONAL):** input data file path (default: src/main/resources/input.txt)
    * **Argument 2 (OPTIONAL):** output file path (default: result.txt)
    * **Argument 3 (OPTIONAL):** enable steps output mode (default: false)

* `console` - run tetris with console input and output
    * **Argument 1 (OPTIONAL):** enable steps output mode (default: false)

To exit the container, write `exit` command.

_To stop the container, write `docker stop tetris`._

## Continuous integration

There are also continuous integration configured on this repository in `/.github/workflows/maven.yml` via GitHub Actions.

You can see the result of executing tests that are placed in `/src/test/kotlin` directory:
* [Successful commit](https://github.com/future-stardust/im-2x-lab-4-kol-oss/actions/runs/12324350710/job/34401747532)
* [Failed commit](https://github.com/future-stardust/im-2x-lab-4-kol-oss/actions/runs/12324763987/job/34402889443)
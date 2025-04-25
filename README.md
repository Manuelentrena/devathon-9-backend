# 🧙‍♂️ Expelliarmicus API - Game Card Online 🎴🃏🀄

![Portada](https://res.cloudinary.com/manuelentrena/image/upload/v1745570854/mural_d7amok.png)

## ✨ Description

This project is an online card game API that simulates the traditional game of rock-paper-scissors, but with a Harry Potter theme. It was created for the 9th edition of Devathon, organized by Pedro Plasencia from **"Programación en Español"**.

🔗 Link del canal: [YouTube - Programación en Español](https://www.youtube.com/@programacion-es)

## 🧙‍♂️ Rules

In this spell-casting game, each spell has an advantage over another. Choose your magic wisely!

- 🛡️ **Protego** beats ✨ **Expelliarmus**  
- ✨ **Expelliarmus** beats 💀 **Avada Kedavra**  
- 💀 **Avada Kedavra** beats 🛡️ **Protego**

The system follows a magical version of _rock-paper-scissors_.

## 📽️ Presentation

Learn more about the concept, rules, and design process behind the game through this Canva presentation:

👉 [Click here to view the presentation](https://www.canva.com/design/DAGlM_bP3Xk/DaCQZTxPs3K2RKcwRBzr9A/view?utm_content=DAGlM_bP3Xk&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=h5c6bdd8757)

👉 [Click here to flow design](https://www.figma.com/design/oGe8qorVrrKxgy7sUFSJyd/Untitled?node-id=0-1&p=f&t=pQSmWI0LLKMgjdUg-0)

It includes:
- 🎮 Game mechanics
- 🧙 Spell interactions
- 🎨 Visual style and inspiration
- 🛠️ Development notes

Take a look to better understand the magic behind the game!

## 🎯 Objectives

* Be able to **connect different users from different clients** to the game through WebSockets.
* Implement an **architecture capable of handling a large number of requests**.
* **Manage the entire server state** — including players, rooms, attacks, rounds, etc. — using RAM memory.

## 🛠️ Technologies

* **Backend:** Spring Boot
* **Websockets:** STOMP Protocol
* **Database:** Mysql 8.0
* **Version Control:** Git
* **Repository:** GitHub

## 📂 Project Documentation

The detailed technical includes:

* **Stomp Protoco Guide:** [Stomp Protocol](https://docs.spring.io/spring-framework/reference/web/websocket/stomp.html)
* **Stomp.js Library:** [Stomp JSl](docs/STOMP-JS.md)
* **Application Interface Agreement (API):** [docs/endpoints.md](docs/ENDPOINTS.md)

## 🧰 Tools Testing

Here are some tools that may be helpful for understanding and working with this project:

* **Stomp Client (for testing API Websocket):** [Stomp Client](https://cdiptangshu.github.io/stomp-client/)
* **WIZARD HAT Global State (Internal tool designed to test the internal state of the project):** [WIZARD HAT Global State](http://localhost:8080/magic-hat/index.html). 
* You can access to this internal tool at this URL after starting the server `http://localhost:8080/magic-hat/index.html`


## ⚙️ Manual Installation

Follow these steps to set up the project in your local environment:

**Prerequisites:**

* Java (version 17)
* Maven (Java dependency manager)
* MySql 8.0 (or MySQL) installed and running
* Git

**Installation Steps:**

1.  **Clone the repository:**

    ```bash
    git clone git@github.com:Manuelentrena/devathon-9-backend.git
    ```

2.  **Navigate to the project directory:**

    ```bash
    cd griffindor-backend
    ```

3.  **Verify the Java version:**

    ```bash
    java -version
    ```

    Ensure you have Java 17 or higher installed.

4.  **Verify the Maven version:**

    ```bash
    mvn -v
    ```

    Make sure Maven (3.8+) is available.

5.  **Configure the .end file:**

    Copy the `.env.example` file to `.env`:

    ```bash
    cp .env.example .env
    ```

    Open the `.env` file and configure the database environment variables (URL_DATABASE, USERNAME_DATABASE, PASSWORD_DATABASE,).

6.  **Edit the application.properties:**

    ```bash
    spring.datasource.url=${URL_DATABASE}
    spring.datasource.username=${USERNAME_DATABASE}
    spring.datasource.password=${PASSWORD_DATABASE}
    ```

7.  **Run the application:**

    ```bash
    mvn spring-boot:run
    ```
    This will start the server at `http://localhost:8080`.

## 🐳 Docker Installation

If you prefer to use Docker for an isolated development environment, follow these steps:

**Prerequisites:**

* Docker installed on your operating system.

**Docker Installation Steps:**

1.  **Verify the Docker version:**

    ```bash
    docker --version
    docker compose version
    ```

    Ensure you have Docker version 20.x or higher.

2.  **Clone the repository:**

    ```bash
    git clone git@github.com:Manuelentrena/devathon-9-backend.git
    ```

3.  **Navigate to the project directory:**

    ```bash
    cd griffindor-backend
    ```

4.  **Configure environment variables:**

    * Copy the `.env.example` file to `.env`:

        ```bash
        cp .env.example .env
        ```

    * Open the `.env` file and configure the necessary environment variables.

5.  **Configure environment variables for the Mysql database:**

    Copy the `.env-example` file to `.env`:

    This is an example for a Mysql enviroment
    ```bash
    DB_NAME=griffindor                  
    DB_USER=griffindor_user                  
    DB_PASS=griffindor_password                 
    DB_HOST=localhost                  
    DB_PORT=3306                  
    DB_ROOT_PASSWORD=root
    ```

6.  **Ejecute Docker compose:**

    ```bash
    docker compose -f docker-compose.dev.yml up -d
    ```

    This will build the Docker images (if it's the first time) and run the containers in the background.

7. **Verify containers is running:**
    
    ```bash
    docker compose -f docker-compose.dev.yml ps
    ``` 
    you should see the containers active running, for example:

    ```bash
    NAME                        COMMAND                  STATUS              PORTS
    mysql-db                    "docker-entrypoint.s…"   Up                  3306/tcp, 33060/tcp
    ```

8. **Stop containers:**
    
    ```bash
    docker compose -f docker-compose.dev.yml down
    ```

9. **If you modified docker-compose file:**
    
    ```bash
    docker compose -f docker-compose.dev.yml up -d --build
    ```

## 🚀 Usage

Once the server is running (either locally or with Docker), you can access the application through your browser at `http://localhost:8080`.

To interact with the API Websocket, consult the interfacewebsockets (API) documentation in [docs/endpoints.md](docs/ENDPOINTS.md) to learn about the available endpoints, HTTP methods, and data formats.

You can use the online tool STOMP Client to simulate a flow of the application `[Stomp Client](https://cdiptangshu.github.io/stomp-client/)

## 🤝 Contribution

If you would like to contribute to this project, please follow these steps:

1.  **Fork** the repository if you are not part of the original team.
2.  **Create a branch** for your contribution: `git checkout -b feat/feature-name`.
3.  **Make your changes** and commit them: `git commit -m "feat: Clear description of the changes made"`.
4.  **Push your changes** to your fork: `git push origin feat/feature-name`.
5.  **Create a Pull Request** to the `develop` branch of the original repository.

Please ensure that you follow the established commit conventions and that your code is well-documented.
* **Commit Convention:** We are using the Conventional Commits convention for a cleaner and more readable commit history. [https://www.conventionalcommits.org/en/v1.0.0/](https://www.conventionalcommits.org/en/v1.0.0/)


---

*Developed with ❤ by the Gryffindor Team for Devathon Edition 9, 2025*

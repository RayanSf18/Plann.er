<h1 align="center" style="font-weight: bold;">Plann.er 💻</h1>

<p align="center">
 <a href="#functionalities">Functionalities</a> • 
 <a href="#technologies">Technologies</a> • 
 <a href="#getting-started">Getting Started</a> • 
 <a href="#api-endpoints">API Endpoints</a> •
 <a href="#developer">Developer</a> •
 <a href="#contribute">Contribute</a>
</p>

<p align="center">
    <b>Plann.er is an application developed to help users organize their trips, whether for work or leisure.</b>
</p>

<h2 id="functionalities">💻 Functionalities</h2>

- **Create Trips:** Register multiple trips with all the necessary details.
- **Update Trips:** Edit trip information at any time.
- **Delete Trips:** Remove trips that are no longer necessary.
- **Invite Participants:** Send invitations to other people to participate in the trip.
- **Add Activities:** Enter specific activities for each day of the trip. This functionality can be used by both the trip creator and the participants.
- **Manage Important Links:** Add and share relevant links such as accommodation reservations, places to visit and other useful information. Both the trip creator and participants can manage these links.

<h2 id="technologies">💻 Technologies</h2>

- Java 17
- H2 Database
- Spring Framework  3.3.2
- Spring Web 
- Spring Data JPA
- Flyway
- Swagger 2.5.0
- Apache Maven 3.3.2
- Lombok
- Spring Validation I/O
- GIT 2.34.1
- ProblemDetail

<h2 id="getting-started">🚀 Getting started</h2>

This section guides you through running the project locally.

<h3>Pre-requisites</h3>

Before you begin, ensure you have the following software installed:

* Java Development Kit (JDK) -  https://www.oracle.com/java/technologies/downloads/
* Maven - https://maven.apache.org/download.cgi

**Optional:**
* IDE (Integrated Development Environment) - (e.g., IntelliJ IDEA, Eclipse)

<h3>Running the Project</h3>

1.  **Clone the Repository:**
```
git clone git@github.com:RayanSf18/Plann.er.git
```
2. **Navigate to the Project Directory:**
```
cd planner
```
3. **Start the Application:**
```
cd planner

mvn spring-boot:run
```
5. **server of application:**
```
http://localhost:8080/
```

<h2 id="api-endpoints">📍 API Endpoints</h2>

<p>View endpoint results in:  <a href="http://localhost:8080/swagger-ui/index.html#/">Swagger</a></p>

| route               | description                                          
|----------------------|-----------------------------------------------------
| <kbd>POST /trips</kbd>     | This endpoint creates a new trip with the provided details.
| <kbd>GET /trips/{tripId}</kbd>     | Fetches the details of a trip using its unique identifier (UUID).
| <kbd>GET /trips</kbd>     | This endpoint searches for all trips within a date range, or if no parameter is passed, returns all trips.
| <kbd>GET /trips/{tripId}/confirm</kbd>     | This endpoint serves to confirm that a trip will occur after its creation.
| <kbd>PUT /trips/{tripId}</kbd>     | This endpoint allows us to update data from an existing trip.
| <kbd>DELETE /trips/{tripId}</kbd>     | This endpoint allows you to delete an existing trip by id
| <kbd>POST /trips/{tripId}/activities</kbd>     | This endpoint is used to add a new activity to an existing trip by id.
| <kbd>GET /trips/{tripId}/activities</kbd>     | This endpoint searches for all activities registered in an existing trip by ID.
| <kbd>POST /trips/{tripId}/invite</kbd>     | This endpoint is for inviting a new participant via email to the existing trip by ID.
| <kbd>GET /trips/{tripId}/participants</kbd>     | This endpoint searches for all participants registered in an existing trip by ID.
| <kbd>POST /trips/{tripId}/links</kbd>     | This endpoint is used to add a new link to an existing trip by id.
| <kbd>GET /trips/{tripId}/links</kbd>     | This endpoint searches for all links registered in an existing trip by ID.
| <kbd>GET /participants/{participantId}/confirm</kbd>     | This endpoint serves to confirm a participant after being invited on the trip.

<h2 id="developer">👨‍💻 Developer</h2>
<table>
  <tr>
    <td align="center">
      <a href="#">
        <img src="https://avatars.githubusercontent.com/u/127986772?v=4" width="100px;" alt="Rayan Silva Profile Picture"/><br>
        <sub>
          <b>Rayan silva</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

<h2 id="contribute">🤝 Contribute</h2>

1. Fork the repository.
2. Create a new branch (git checkout -b feature/AmazingFeature).
3. Make your changes.
4. Commit your changes (git commit -m 'Add some AmazingFeature').
5. Push to the branch (git push origin feature/AmazingFeature).
6. Open a pull request.

<h3>Documentations that might help</h3>

[📝 How to create a Pull Request](https://www.atlassian.com/br/git/tutorials/making-a-pull-request)

[💾 Commit pattern](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716)

Enjoy coding! 😄

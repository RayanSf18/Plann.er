<h1 align="center" style="font-weight: bold;">Plann.er 💻</h1>

<p align="center">
 <a href="#technologies">Technologies</a> • 
 <a href="#getting-started">Getting Started</a> • 
 <a href="#api-endpoints">API Endpoints</a> •
 <a href="#developer">Developer</a> •
 <a href="#contribute">Contribute</a>
</p>

<p align="center">
    <b>Plann.er is a backend application developed to help users organize their trips, whether for work or leisure. With Plann.er, you can create trips, add activities for each day and invite participants, making it easier to manage and plan every detail of the trip.</b>
</p>

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
| <kbd>POST /trips</kbd>     | create a new trip 
| <kbd>GET /trips/{tripId}</kbd>     | search for a specific trip by ID
| <kbd>GET /trips</kbd>     | Search all trips 
| <kbd>GET trips/{tripId}/confirm</kbd>     | Confirm the trip
| <kbd>PUT /trips/{tripId}</kbd>     | update trip data by id 
| <kbd>DELETE /trips/{tripId}</kbd>     | delete a trip by id
| <kbd>POST /trips/{tripId}/activities</kbd>     | create a new activity
| <kbd>GET /trips/{tripId}/activities</kbd>     | search all Activities from a trip by ID 
| <kbd>POST /trips/{tripId}/invite</kbd>     | invite a new participant
| <kbd>GET /trips/{tripId}/participants</kbd>     | search all Participants from a trip by ID
| <kbd>POST /trips/{tripId}/links</kbd>     | create a new link
| <kbd>GET /trips/{tripId}/links</kbd>     | search all links from a trip by ID 




| <kbd>PUT /categories/{categoryId}</kbd>     | update data for a specific category [request details](#put-category)
| <kbd>DELETE /categories/{categoryName}</kbd>     | deletes a specific category that has not yet been added to an product [request details](#delete-category)
| <kbd>POST /orders</kbd>     | create a new order [request details](#post-order-create)
| <kbd>GET /orders/{orderId}</kbd>     | search for a specific order [response details](#get-order-detail)
| <kbd>GET /orders</kbd>     | search all orders [response details](#get-orders-details)
| <kbd>POST /orders/{orderId}/items?productId={productId}&quantity={quantity}</kbd>     | add product to order [request details](#post-add-product-to-order)
| <kbd>POST /orders/{orderId}/items/remove?productId={productId}</kbd>     | remove product to order [request details](#post-remove-product-to-order)
| <kbd>POST /orders/{orderId}/payment</kbd>     | add payment to order [request details](#post-add-payment-to-order)
| <kbd>POST /orders/{orderId}/cancel</kbd>     | delete order [request details](#delete-order)

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

import java.util.UUID

import org.junit.Test
import politrons.scalaydrated.PersistenceModel._
import politrons.scalaydrated.command.{AddProductCommand, CreateUserCommand, RemoveProductCommand}
import politrons.scalaydrated.event.{ProductAdded, ProductRemoved, UserCreated}
import politrons.scalaydrated.model.User
import politrons.scalaydrated.persistence.CouchbaseDAO

/**
  * Created by pabloperezgarcia on 28/10/2016.
  *
  * In those particular example we use the three unique methods of the API
  * 1º {createDocument} Create an empty document with an array of empty Events
  * 2º {appendEvent} Append an event into the document and pass the function to be applied in the rehydrate
  * 3º {rehydrate} Rehydrate the model with all events persisted
  */
class EventSourcingTest {

  val couchbase= new CouchbaseDAO()

  @Test
  def createAccountTest() {
    //Given
    val user = initialize[User](couchbase)
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val createUserCommand = new CreateUserCommand(userName, password)
    //When
    user.appendEvent[UserCreated](createUserCommand)
    user.rehydrate(id)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
  }


  @Test
  def shoppingTest() {
    //Given
    val user = initialize[User](couchbase)
    user.createDocument(generateId)
    val createProductCmd = new AddProductCommand(generateId, "Beans", "1.00")
    //When
    user.appendEvent[ProductAdded](createProductCmd)
    user.rehydrate()
    //Then
    assert(user.products.size == 1)
  }

  @Test
  def completeRehydrateTest() {
    //Given
    val user = initialize[User](couchbase)
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val createUserCommand = new CreateUserCommand(userName, password)
    val createProductCmd = new AddProductCommand(generateId, "Beans", "1.00")
    //When
    user.appendEvent[UserCreated](createUserCommand)
    user.appendEvent[ProductAdded](createProductCmd)
    user.rehydrate()
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 1)
    assert(user.products.head.productName.equals("Beans") && user.products.head.productPrice.equals("1.00"))

  }

  @Test
  def addThreeProducts() {
    //Given
    val user = initialize[User](couchbase)
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val createUserCommand = new CreateUserCommand(userName, password)
    val createProductCmd = new AddProductCommand(generateId, "Beans", "1.00")
    //When
    user.appendEvent[UserCreated](createUserCommand)
    user.appendEvent[ProductAdded](createProductCmd)
    user.appendEvent[ProductAdded](createProductCmd)
    user.appendEvent[ProductAdded](createProductCmd)
    user.rehydrate(id)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 3)
  }

  @Test
  def addRemoveProducts() {
    //Given
    val user = initialize[User](couchbase)
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val createUserCommand = new CreateUserCommand(userName, password)
    //Add products events
    val productId1: String = generateId
    val createProductCmd1 = new AddProductCommand(productId1, "Beans", "1.00")

    val productId2: String = generateId
    val createProductCmd2 = new AddProductCommand(productId2, "Coca-cola", "3.00")

    val productId3: String = generateId
    val createProductCmd3 = new AddProductCommand(productId3, "Pizza", "6.00")

    val productId4: String = generateId
    val createProductCmd4 = new AddProductCommand(productId4, "Playstation 4", "399.00")
    //Remove products events
    val productRemovedCmd1 = new RemoveProductCommand(productId1)
    val productRemovedCmd2 = new RemoveProductCommand(productId3)
    //When
    user.appendEvent[UserCreated](createUserCommand)
    user.appendEvent[ProductAdded](createProductCmd1)
    user.appendEvent[ProductAdded](createProductCmd2)
    user.appendEvent[ProductAdded](createProductCmd3)
    user.appendEvent[ProductRemoved](productRemovedCmd1)
    user.appendEvent[ProductAdded](createProductCmd4)
    user.appendEvent[ProductRemoved](productRemovedCmd2)

    val newUser = initialize[User](couchbase)
    newUser.rehydrate(id)
    //Then
    assert(newUser.userName.equals(userName) && newUser.password.equals(password))
    assert(newUser.products.size == 2)

    val totalMoney = newUser.products.map(product => BigDecimal.apply(product.productPrice)).sum
    assert(totalMoney == 402.00)

  }

  private def getCredentials: (String, String, String) = {
    val userName = generateId
    val password = "password"
    val id = userName + password
    (userName, password, id)
  }

  private def generateId: String = {
    UUID.randomUUID().toString
  }


}

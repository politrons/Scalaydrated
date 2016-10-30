import java.util.UUID

import event.{ProductAdded, ProductRemoved, UserCreated}
import model.User
import org.junit.Test
import persistence.CouchbaseDAO
import politrons.scalaydrated.PersistenceModel._

/**
  * Created by pabloperezgarcia on 28/10/2016.
  *
  * In those particular example we use the three unique methods of the API
  * 1º {createDocument} Create an empty document with an array of empty Events
  * 2º {appendEvent} Append an event into the document and pass the function to be applied in the rehydrate
  * 3º {rehydrate} Rehydrate the model with all events persisted
  */
class EventSourcingTest {

  /**
    * Return the actor model to be rehydrate
    */
  val user = initialize[User](new CouchbaseDAO())

  @Test
  def createAccountTest() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val event = new UserCreated(userName, password)
    //When
//    addUserCreatedEvent(documentId, event)
    user.appendEvent[UserCreated, User](event,
      (model, evt) => model.loadAccount(evt.userName, evt.password))
    user.rehydrate(id)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
  }


  @Test
  def shoppingTest() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val event = new ProductAdded(generateId, "Beans", "1.00")
    //When
    addProductEvent(event)
    user.rehydrate()
    //Then
    assert(user.products.size == 1)
  }

  @Test
  def completeRehydrateTest() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val userCreatedEvent = new UserCreated(userName, password)
    val productAddedEvent = new ProductAdded(generateId, "Beans", "1.00")
    //When
    addUserCreatedEvent(userCreatedEvent)
    addProductEvent(productAddedEvent)
    user.rehydrate()
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 1)
    assert(user.products.head.productName.equals("Beans") && user.products.head.productPrice.equals("1.00"))

  }

  @Test
  def addThreeProducts() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val userCreatedEvent = new UserCreated(userName, password)
    val productAddedEvent = new ProductAdded(generateId, "Beans", "1.00")
    //When
    addUserCreatedEvent(userCreatedEvent)
    addProductEvent(productAddedEvent)
    addProductEvent(productAddedEvent)
    addProductEvent(productAddedEvent)
    user.rehydrate(id)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 3)
  }

  @Test
  def addRemoveProducts() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    user.createDocument(id)
    val userCreatedEvent = new UserCreated(userName, password)
    //Add products events
    val productId1: String = generateId
    val productAddedEvent1 = new ProductAdded(productId1, "Beans", "1.00")

    val productId2: String = generateId
    val productAddedEvent2 = new ProductAdded(productId2, "Coca-cola", "3.00")

    val productId3: String = generateId
    val productAddedEvent3 = new ProductAdded(productId3, "Pizza", "6.00")

    val productId4: String = generateId
    val productAddedEvent4 = new ProductAdded(productId4, "Playstation 4", "399.00")
    //Remove products events
    val removeProduct1 = new ProductRemoved(productId1)
    val removeProduct2 = new ProductRemoved(productId3)


    //When
    addUserCreatedEvent(userCreatedEvent)
    addProductEvent(productAddedEvent1)
    addProductEvent(productAddedEvent2)
    addProductEvent(productAddedEvent3)
    removeProductEvent(removeProduct1)
    addProductEvent(productAddedEvent4)
    removeProductEvent(removeProduct2)
    user.rehydrate()

    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 2)

    val totalMoney = user.products.map(product => BigDecimal.apply(product.productPrice)).sum
    assert(totalMoney == 402.00)

  }

  private def addUserCreatedEvent(event: UserCreated): Unit = {
    user.appendEvent[UserCreated, User](event,
      (model, evt) => model.loadAccount(evt.userName, evt.password))
  }

  private def addProductEvent(event: ProductAdded): Unit = {
    user.appendEvent[ProductAdded, User](event,
      (model, evt) => model.loadProduct(evt.productId, evt.productName, evt.productPrice))
  }


  private def removeProductEvent(event: ProductRemoved): Unit = {
    user.appendEvent[ProductRemoved, User](event,
      (model, evt) => model.removeProduct(evt.productId))
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

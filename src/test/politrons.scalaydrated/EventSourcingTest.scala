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
    val documentId: String = user.createDocument(id)
    val event = new UserCreated(userName, password)
    //When
    addUserCreatedEvent(documentId, event)
    user.rehydrate(documentId)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
  }


  @Test
  def shoppingTest() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    val documentId: String = user.createDocument(id)
    val event = new ProductAdded(UUID.randomUUID().toString, "Beans", "1.00")
    //When
    addProductEvent(id, event)
    user.rehydrate(documentId)
    //Then
    assert(user.products.size == 1)
  }

  @Test
  def completeRehydrateTest() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    val documentId: String = user.createDocument(id)
    val userCreatedEvent = new UserCreated(userName, password)
    val productAddedEvent = new ProductAdded(UUID.randomUUID().toString, "Beans", "1.00")
    //When
    addUserCreatedEvent(documentId, userCreatedEvent)
    addProductEvent(id, productAddedEvent)
    user.rehydrate(documentId)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 1)
    assert(user.products.head.productName.equals("Beans") && user.products.head.productPrice.equals("1.00"))

  }

  @Test
  def threeBeansCan() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    val documentId: String = user.createDocument(id)
    val userCreatedEvent = new UserCreated(userName, password)
    val productAddedEvent = new ProductAdded(UUID.randomUUID().toString, "Beans", "1.00")
    //When
    addUserCreatedEvent(documentId, userCreatedEvent)
    addProductEvent(id, productAddedEvent)
    addProductEvent(id, productAddedEvent)
    addProductEvent(id, productAddedEvent)
    user.rehydrate(documentId)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 3)
  }

  @Test
  def addRemoveProducts() {
    //Given
    val (userName: String, password: String, id: String) = getCredentials
    val documentId: String = user.createDocument(id)
    val userCreatedEvent = new UserCreated(userName, password)
    //Add products events
    val productId1: String = UUID.randomUUID().toString
    val productAddedEvent1 = new ProductAdded(productId1, "Beans1", "1.00")

    val productId2: String = UUID.randomUUID().toString
    val productAddedEvent2 = new ProductAdded(productId2, "Beans2", "1.00")

    val productId3: String = UUID.randomUUID().toString
    val productAddedEvent3 = new ProductAdded(productId3, "Beans3", "1.00")

    val productId4: String = UUID.randomUUID().toString
    val productAddedEvent4 = new ProductAdded(productId4, "Beans4", "1.00")
    //Remove products events
    val removeProcut1 = new ProductRemoved(productId1)
    val removeProcut2 = new ProductRemoved(productId3)


    //When
    addUserCreatedEvent(documentId, userCreatedEvent)
    addProductEvent(id, productAddedEvent1)
    addProductEvent(id, productAddedEvent2)
    addProductEvent(id, productAddedEvent3)
    removeProductEvent(id, removeProcut1)
    addProductEvent(id, productAddedEvent4)
    removeProductEvent(id, removeProcut2)
    user.rehydrate(documentId)

    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
    assert(user.products.size == 2)

//    val totalMoney = user.products.map(product => BigDecimal.apply(product.productPrice)).sum
//    assert(totalMoney == 2)

  }

  def addUserCreatedEvent(documentId: String, event: UserCreated): Unit = {
    user.appendEvent[UserCreated, User](documentId, event, classOf[UserCreated],
      (model, evt) => model.loadAccount(evt.userName, evt.password))
  }

  def addProductEvent(id: String, event: ProductAdded): Unit = {
    user.appendEvent[ProductAdded, User](id, event, classOf[ProductAdded],
      (model, evt) => model.loadProduct(evt.productId, evt.productName, evt.productPrice))
  }


  def removeProductEvent(id: String, event: ProductRemoved): Unit = {
    user.appendEvent[ProductRemoved, User](id, event, classOf[ProductRemoved],
      (model, evt) => model.removeProduct(evt.productId))
  }

  def getCredentials: (String, String, String) = {
    val userName = UUID.randomUUID().toString
    val password = "password"
    val id = userName + password
    (userName, password, id)
  }


}

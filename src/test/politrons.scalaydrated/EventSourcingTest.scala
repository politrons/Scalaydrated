import java.util.UUID

import command.CreateUserCommand
import event.UserCreated
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
    val createUserCommand = new CreateUserCommand(userName, password)
    user.appendEvent[CreateUserCommand, User, UserCreated](createUserCommand)
    user.rehydrate(id)
    //Then
    assert(user.userName.equals(userName) && user.password.equals(password))
  }

//
//  @Test
//  def shoppingTest() {
//    //Given
//    val (userName: String, password: String, id: String) = getCredentials
//    user.createDocument(id)
//    val event = new ProductAdded(generateId, "Beans", "1.00")
//    //When
//    user.appendEvent[ProductAdded, User](event, (model, evt) => model.load(evt))
//    user.rehydrate()
//    //Then
//    assert(user.products.size == 1)
//  }
//
//  @Test
//  def completeRehydrateTest() {
//    //Given
//    val (userName: String, password: String, id: String) = getCredentials
//    user.createDocument(id)
//    val createUserCommand = new CreateUserCommand(userName, password)
//    val productAddedEvt = ProductAdded(generateId, "Beans", "1.00")
//    //When
//    user.appendEvent[CreateUserCommand, User, UserCreated](createUserCommand, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt, (model, evt) => model.load(evt))
//    user.rehydrate()
//    //Then
//    assert(user.userName.equals(userName) && user.password.equals(password))
//    assert(user.products.size == 1)
//    assert(user.products.head.productName.equals("Beans") && user.products.head.productPrice.equals("1.00"))
//
//  }
//
//  @Test
//  def addThreeProducts() {
//    //Given
//    val (userName: String, password: String, id: String) = getCredentials
//    user.createDocument(id)
//    val userCreatedEvt = new UserCreated(userName, password)
//    val productAddedEvt = new ProductAdded(generateId, "Beans", "1.00")
//    //When
//    user.appendEvent[UserCreated, User](userCreatedEvt, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt, (model, evt) => model.load(evt))
//    user.rehydrate(id)
//    //Then
//    assert(user.userName.equals(userName) && user.password.equals(password))
//    assert(user.products.size == 3)
//  }
//
//  @Test
//  def addRemoveProducts() {
//    //Given
//    val (userName: String, password: String, id: String) = getCredentials
//    user.createDocument(id)
//    val userCreatedEvt = new UserCreated(userName, password)
//    //Add products events
//    val productId1: String = generateId
//    val productAddedEvt1 = new ProductAdded(productId1, "Beans", "1.00")
//
//    val productId2: String = generateId
//    val productAddedEvt2 = new ProductAdded(productId2, "Coca-cola", "3.00")
//
//    val productId3: String = generateId
//    val productAddedEvt3 = new ProductAdded(productId3, "Pizza", "6.00")
//
//    val productId4: String = generateId
//    val productAddedEvt4 = new ProductAdded(productId4, "Playstation 4", "399.00")
//    //Remove products events
//    val productRemovedEvt1 = new ProductRemoved(productId1)
//    val productRemovedEvt2 = new ProductRemoved(productId3)
//    //When
//    user.appendEvent[UserCreated, User](userCreatedEvt, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt1, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt2, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt3, (model, evt) => model.load(evt))
//    user.appendEvent[ProductRemoved, User](productRemovedEvt1, (model, evt) => model.load(evt))
//    user.appendEvent[ProductAdded, User](productAddedEvt4, (model, evt) => model.load(evt))
//    user.appendEvent[ProductRemoved, User](productRemovedEvt2, (model, evt) => model.load(evt))
//    user.rehydrate()
//    //Then
//    assert(user.userName.equals(userName) && user.password.equals(password))
//    assert(user.products.size == 2)
//
//    val totalMoney = user.products.map(product => BigDecimal.apply(product.productPrice)).sum
//    assert(totalMoney == 402.00)
//
//  }

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

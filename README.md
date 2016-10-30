Author  Pablo Perez Garcia 

![My image](src/main/resources/img/Scalaydrated.png)

Event sourcing project
 
 
The use of this library is really simple, you just need to follow the next steps in order to start playing.
 * Create a model class(Akka actor). Must extends Model library class.
 * Create all events class that you want to apply on your model. Must extend Event library class.
   * `initialize` passing the type of the model and an instance of the persistence layer, we create a new model instance we invoke the init of the persistence layer, and we add the persistence layer into the model.
 * Once that you have that model, this one contains the methods of the API 
   * `createDocument` Create an empty document with an array of empty Events
   * `appendEvent` Append an event into the document and pass the function to be applied in the rehydrate
   * `rehydrate` Rehydrate the model with all events persisted
   
   
In order to do not impose Json library to the developers, the encoded document is passed to the persistence layer in String format.

The document is formed as json structure, where contains time of creation of the document and array of events to be used for the rehydration of the model.  

An example of a Document with several events imitating an online shopping: 
```   
{
  "events": [
    {
      "event": "event.UserCreated",
      "userName": "72c92626-aac3-4e06-8d00-37bf874bc6ec",
      "password": "password"
    },
    {
      "productId": "494e9d04-dd2d-40f6-ba03-55e57a44bc2f",
      "event": "event.ProductAdded",
      "productName": "Beans",
      "productPrice": "1.00"
    },
    {
      "productId": "f3b27f69-a75c-4791-b198-cc9b6a0dbeff",
      "event": "event.ProductAdded",
      "productName": "Coca-cola",
      "productPrice": "3.00"
    },
    {
      "productId": "fb9b6975-403a-4698-b308-746bbcacb279",
      "event": "event.ProductAdded",
      "productName": "Pizza",
      "productPrice": "6.00"
    },
    {
      "productId": "494e9d04-dd2d-40f6-ba03-55e57a44bc2f",
      "event": "event.ProductRemoved"
    },
    {
      "productId": "e898a333-42e1-419f-a7b4-e2012caabdd4",
      "event": "event.ProductAdded",
      "productName": "Playstation 4",
      "productPrice": "399.00"
    },
    {
      "productId": "fb9b6975-403a-4698-b308-746bbcacb279",
      "event": "event.ProductRemoved"
    }
  ],
  "time": "Sun Oct 30 10:00:27 GMT 2016"
}
```   

Unit test [here] (src/test/politrons.scalaydrated/EventSourcingTest.scala)


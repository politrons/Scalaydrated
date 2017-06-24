Author  Pablo Perez Garcia 

![My image](src/main/resources/img/Scalaydrated.png)

CQRS + Event sourcing project
 
The use of this library is really simple, you just need to follow the next steps in order to start playing.
 * Classes to implement in order to use this library:
    * Create a model class(Akka actor). Must extends `Model` library class.
    * Create all events class that you want to apply on your model with the actions that you want to apply over it. Must extend `Event` library class.
    * Create all commands to orchestrate the creation of the event to be used for the rehydration of the model. Must extend `Command` library class.
 * Once that you have all your classes implemented is time to start playing.
   * `initialize` passing the type of the model and an instance of the persistence layer, we create a new model instance we invoke the init of the persistence layer, and we add the persistence layer into the model.
 * Once that you have that model, this one contains the methods of the API 
   * `createDocument` Create an empty document with an array of empty Events
   * `appendEvent` Append an event into the document and passed through a command to be applied in the rehydrate
   * `rehydrate` Rehydrate the model with all events persisted
   
In order to do not impose Json library to the developers, the encoded document is passed to the persistence layer in String format.

The document is formed as json structure, where contains time of creation of the document and array of events to be used for the rehydration of the model.  

An example of a Document with several events imitating an online shopping: 
```   
{
  "events": [
    {
      "password": "password",
      "time": "Wed Nov 02 18:58:02 GMT 2016",
      "event": "event.UserCreated",
      "userName": "9d57f821-61b8-455c-9cf0-2868b1dc2023"
    },
    {
      "time": "Wed Nov 02 18:58:04 GMT 2016",
      "productId": "0d1c0bb1-a5f8-454b-b9b8-b12f7a87022b",
      "event": "event.ProductAdded",
      "productName": "Beans",
      "productPrice": "1.00"
    },
    {
      "time": "Wed Nov 02 18:58:04 GMT 2016",
      "productId": "dbb3f27c-dfc9-4d72-a2ed-f02bb2ec3238",
      "event": "event.ProductAdded",
      "productName": "Coca-cola",
      "productPrice": "3.00"
    },
    {
      "time": "Wed Nov 02 18:58:04 GMT 2016",
      "productId": "a40340f4-1b38-4251-bc1f-293d72d56448",
      "event": "event.ProductAdded",
      "productName": "Pizza",
      "productPrice": "6.00"
    },
    {
      "productId": "0d1c0bb1-a5f8-454b-b9b8-b12f7a87022b",
      "event": "event.ProductRemoved",
      "time": "Wed Nov 02 18:58:04 GMT 2016"
    },
    {
      "time": "Wed Nov 02 18:58:04 GMT 2016",
      "productId": "56d71d56-0f69-42ad-9a1a-2e89e78a86cb",
      "event": "event.ProductAdded",
      "productName": "Playstation 4",
      "productPrice": "399.00"
    },
    {
      "productId": "a40340f4-1b38-4251-bc1f-293d72d56448",
      "event": "event.ProductRemoved",
      "time": "Wed Nov 02 18:58:04 GMT 2016"
    }
  ],
  "time": "Wed Nov 02 18:58:02 GMT 2016"
}
```   

Unit test [here](src/test/scala/EventSourcingTest.scala)


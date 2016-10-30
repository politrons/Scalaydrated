Author  Pablo Perez Garcia 

![My image](src/main/resources/img/Scalaydrated.png)

Event sourcing project
 
 
The use of this library is really simple, you just need to follow the next steps in order to start playing.
 * Create all events that you want to apply on your model extending Event library class.
 * Create a model(Akka actor), that extends Model library class.
 * once that you have that model you can made use the three unique methods of the API
   * {createDocument} Create an empty document with an array of empty Events
   * {appendEvent} Append an event into the document and pass the function to be applied in the rehydrate
   * {rehydrate} Rehydrate the model with all events persisted
   
   
In order to do not impose Json library to the developer, the encode document passed to the persistence layer is in String format.

The document is formed with json structure, where contains timestamp of creation of the document and array of events to be used for the rehydrate of the model.  

An example of a Document with several events imitating a basket: 
```   
  {
    "events": [
      {
        "event": "event.UserCreated",
        "userName": "6baf181a-ee9b-4c7f-a341-f23d572914d3",
        "password": "password"
      },
      {
        "productId": "0c107db8-27ae-4054-a659-eca5bdc74834",
        "event": "event.ProductAdded",
        "productName": "Beans1",
        "productPrice": "1.00"
      },
      {
        "productId": "23c6731d-67c8-44b0-80c2-df014f5da48d",
        "event": "event.ProductAdded",
        "productName": "Beans2",
        "productPrice": "1.00"
      },
      {
        "productId": "c71678b1-db1d-4d08-a293-2db11e474087",
        "event": "event.ProductAdded",
        "productName": "Beans3",
        "productPrice": "1.00"
      },
      {
        "productId": "0c107db8-27ae-4054-a659-eca5bdc74834",
        "event": "event.ProductRemoved"
      },
      {
        "productId": "e57fda0e-fb09-4657-b2fb-f6ffbf1c25d3",
        "event": "event.ProductAdded",
        "productName": "Beans4",
        "productPrice": "1.00"
      },
      {
        "productId": "c71678b1-db1d-4d08-a293-2db11e474087",
        "event": "event.ProductRemoved"
      }
    ]
  }
```   
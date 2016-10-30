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
       "userName": "cad522d4-f773-49b7-b3b5-e59d273b6f37",
       "password": "password"
     },
     {
       "productId": "9b03f020-ffd5-4128-88c7-f9ffef3d9d6f",
       "event": "event.ProductAdded",
       "productName": "Beans",
       "productPrice": "1.00"
     },
     {
       "productId": "e4567899-a411-47f6-a7e1-bba255130157",
       "event": "event.ProductAdded",
       "productName": "Coca-cola",
       "productPrice": "3.00"
     },
     {
       "productId": "e8aad91f-5916-4a40-8b2b-f9a3015eb064",
       "event": "event.ProductAdded",
       "productName": "Pizza",
       "productPrice": "6.00"
     },
     {
       "productId": "9b03f020-ffd5-4128-88c7-f9ffef3d9d6f",
       "event": "event.ProductRemoved"
     },
     {
       "productId": "05db01c2-a1d4-42d4-8068-4f37f085487c",
       "event": "event.ProductAdded",
       "productName": "Playstation 4",
       "productPrice": "399.00"
     },
     {
       "productId": "e8aad91f-5916-4a40-8b2b-f9a3015eb064",
       "event": "event.ProductRemoved"
     }
   ]
 }
```   
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
         "type": "event.UserCreated",
         "userName": "053f7b53-2963-406c-827b-4e15d98bad35",
         "password": "password"
       },
       {
         "productId": "f771def4-8e9c-4995-b810-d5e4d3749a99",
         "type": "event.ProductAdded",
         "productName": "Beans1",
         "productPrice": "1.00"
       },
       {
         "productId": "07111f90-8c20-425e-afd0-b106aa3a141a",
         "type": "event.ProductAdded",
         "productName": "Beans2",
         "productPrice": "1.00"
       },
       {
         "productId": "e7ae4694-8c77-4884-8d17-49d48ef3d7b2",
         "type": "event.ProductAdded",
         "productName": "Beans3",
         "productPrice": "1.00"
       },
       {
         "productId": "f771def4-8e9c-4995-b810-d5e4d3749a99",
         "type": "event.ProductRemoved"
       },
       {
         "productId": "7df83572-6298-4604-ac04-48e6874c6723",
         "type": "event.ProductAdded",
         "productName": "Beans4",
         "productPrice": "1.00"
       },
       {
         "productId": "e7ae4694-8c77-4884-8d17-49d48ef3d7b2",
         "type": "event.ProductRemoved"
       }
     ]
   }
```   
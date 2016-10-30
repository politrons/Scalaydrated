Author  Pablo Perez Garcia 

![My image](src/main/resources/img/Scalaydrated.png)

Event sourcing project
 
 
The use of this library is really simple, you just need to follow the next steps in order to start playing.
 * Create all events that you want to apply on your model extending Event library class.
 * Create a model(Akka actor), that extends Model library class.
 * once that you have that model you can made use the three unique methods of the API
   * ** {createDocument} Create an empty document with an array of empty Events
   * ** {appendEvent} Append an event into the document and pass the function to be applied in the rehydrate
   * ** {rehydrate} Rehydrate the model with all events persisted
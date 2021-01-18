package tweet

// Cases classes used to convert json to Scala objects
case class Tweet(data: Array[Data])
case class Data(id: String, text: String)



/* Used for original temp data, may be useful again at some point

case class Tweet(data: Array[Data], includes: Includes, meta: Meta)
case class Data(author_id: String, created_at: String, id: String, text: String)
case class Includes(users: Array[Users])
case class Users(description: String, id: String, name: String, username: String)
case class Meta(newest_id: String, oldest_id: String, result_count: Long)
*/
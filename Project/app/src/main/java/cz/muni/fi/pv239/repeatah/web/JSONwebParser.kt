package cz.muni.fi.pv239.repeatah.web

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import cz.muni.fi.pv239.repeatah.model.Answer
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.Question
import cz.muni.fi.pv239.repeatah.model.Topic
import org.json.JSONObject
import java.util.*

/**
 * Class for downloading data from Internet Database and parsing it to Objects
 */
class JSONwebParser {

    //URL of Google Script that returns JSON of Internet Database
    private val URL = "https://script.google.com/macros/s/AKfycbw1zOF8sv4MEg9lpFFa-2oeqFKzOAyTDUcA1zjidGdDrVIONQnk/exec?id=${getDatabaseInLanguage()}&sheet="
    //Names of GoogleDoc Sheets
    private val sheets = listOf("Topics", "Drills", "Questions", "Answers")

    //Function for getting JSON from given GoogleDoc sheet
    private fun getDataFromWeb(sheet : String): JSONObject {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(URL + sheet).build()
        val response = okHttpClient.newCall(request).execute()
        val responseString = response.body().string()

        return JSONObject(responseString)
    }

    //Function for getting Topics from Web
    private fun getTopicsJSON(): JSONObject {
        return getDataFromWeb(sheets[0])
    }

    //Function for getting Drills from Web
    private fun getDrillsJSON(): JSONObject {
        return getDataFromWeb(sheets[1])
    }

    //Function for getting Questions from Web
    private fun getQuestionsJSON(): JSONObject {
        return getDataFromWeb(sheets[2])
    }

    //Function for getting Answers from Web
    private fun getAnswersJSON(): JSONObject {
        return getDataFromWeb(sheets[3])
    }

    //Get data in a language according to Locale language
    private fun getDatabaseInLanguage(): String {
        //Get device language
        val language = Locale.getDefault().language
        val databaseId : String
        //Set databaseID according to device language
        when(language){
            "cs" -> databaseId = "1JkhQUwkl1DAI9sfWwVJX4iEv_JWLkeIFJxhv-70Kozk"
            else -> databaseId = "1VpcdihvANirLzqVgy9hMlF8fymxra6-XchdkTaAfIY8"
        }
        return databaseId
    }

    //Function for creating Topic objects from JSON
    fun getTopics(): MutableList<Topic> {
        val json = getTopicsJSON()
        val jsonRecords = json.getJSONArray("records")
        val jsonSize = jsonRecords.length()
        val topics = mutableListOf<Topic>()

        for (i in 0..jsonSize-1){
            val jsonDetail = jsonRecords.getJSONObject(i)

            val topicId = jsonDetail.getString("topic_id").toInt()
            val name = jsonDetail.getString("name")
            val icon = jsonDetail.getString("icon").toInt()
            val colour = jsonDetail.getString("colour").toInt()
            val background = jsonDetail.getString("background").toInt()

            val topic = Topic(topicId, name, icon, colour, background)
            topics.add(topic)
        }

        return topics
    }

    //Function for creating Drill objects from JSON
    fun getDrills(): MutableList<Drill> {
        val json = getDrillsJSON()
        val jsonRecords = json.getJSONArray("records")
        val jsonSize = jsonRecords.length()
        val drills = mutableListOf<Drill>()

        for (i in 0..jsonSize-1){
            val jsonDetail = jsonRecords.getJSONObject(i)

            val drill_id = jsonDetail.getString("drill_id").toInt()
            val drill_topic_id = jsonDetail.getString("drill_topic_id").toInt()
            val name = jsonDetail.getString("name")
            val colour = jsonDetail.getString("colour").toInt()
            val background = jsonDetail.getString("background").toInt()

            val drill = Drill(drill_id, drill_topic_id, name, colour, background)
            drills.add(drill)
        }

        return drills
    }

    //Function for creating Question objects from JSON
    fun getQuestions(): MutableList<Question> {
        val json = getQuestionsJSON()
        val jsonRecords = json.getJSONArray("records")
        val jsonSize = jsonRecords.length()
        val questions = mutableListOf<Question>()

        for (i in 0..jsonSize-1){
            val jsonDetail = jsonRecords.getJSONObject(i)

            val question_id = jsonDetail.getString("question_id").toInt()
            val question_drill_id = jsonDetail.getString("question_drill_id").toInt()
            val text = jsonDetail.getString("text")


            val question = Question(question_id, question_drill_id, text)
            questions.add(question)
        }

        return questions
    }

    //Function for creating Answer objects from JSON
    fun getAnswers(): MutableList<Answer> {
        val json = getAnswersJSON()
        val jsonRecords = json.getJSONArray("records")
        val jsonSize = jsonRecords.length()
        val answers = mutableListOf<Answer>()

        for (i in 0..jsonSize-1){
            val jsonDetail = jsonRecords.getJSONObject(i)

            val answer_id = jsonDetail.getString("answer_id").toInt()
            val answer_question_id = jsonDetail.getString("answer_question_id").toInt()
            val text = jsonDetail.getString("text")
            val correct = jsonDetail.getString("correct").toBoolean()

            val answer = Answer(answer_id, answer_question_id, text, correct)
            answers.add(answer)
        }

        return answers
    }

}
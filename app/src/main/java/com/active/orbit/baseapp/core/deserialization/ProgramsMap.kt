package com.active.orbit.baseapp.core.deserialization

import android.text.TextUtils
import com.active.orbit.baseapp.core.database.models.DBProgram
import com.active.orbit.baseapp.core.database.models.DBSeverity
import com.active.orbit.baseapp.core.database.models.DBSymptom
import com.active.orbit.baseapp.core.generics.BaseModel
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.core.utils.Logger
import com.google.gson.annotations.SerializedName

class ProgramsMap : BaseModel {

    @SerializedName("data")
    var data = ArrayList<ProgramMap>()

    override fun identifier(): String {
        return Constants.EMPTY
    }

    override fun isValid(): Boolean {
        return data.isNotEmpty()
    }

    class ProgramMap : BaseModel {

        @SerializedName("_id")
        var id = Constants.EMPTY

        @SerializedName("name")
        var name = Constants.EMPTY

        @SerializedName("order")
        var order = Constants.INVALID

        @SerializedName("symptoms")
        var symptoms = ArrayList<SymptomMap>()

        override fun identifier(): String {
            return id
        }

        override fun isValid(): Boolean {
            return !TextUtils.isEmpty(id) && !TextUtils.isEmpty(name) && order != Constants.INVALID // TODO restore this -> && symptoms.isNotEmpty()
        }

        fun toDBProgram(): DBProgram {
            val model = DBProgram()
            model.position = order
            model.idProgram = id
            model.name = name
            return model
        }
    }

    class SymptomMap : BaseModel {

        @SerializedName("symptomId")
        var symptomId = Constants.EMPTY

        @SerializedName("question")
        var question = Constants.EMPTY

        @SerializedName("options")
        var options = ArrayList<OptionMap>()

        override fun identifier(): String {
            return symptomId
        }

        override fun isValid(): Boolean {
            return !TextUtils.isEmpty(symptomId) && !TextUtils.isEmpty(question) // TODO restore this -> && options.isNotEmpty()
        }

        fun toDBSymptom(idProgram: String, position: Int): DBSymptom {
            val model = DBSymptom()
            model.idProgram = idProgram
            model.idSymptom = symptomId
            model.position = position
            model.question = question
            return model
        }
    }

    class OptionMap : BaseModel {

        @SerializedName("_id")
        var id = Constants.EMPTY

        @SerializedName("symptomResponse")
        var symptomResponse = Constants.EMPTY

        @SerializedName("symptomValue")
        var symptomValue = Constants.EMPTY

        override fun identifier(): String {
            return id
        }

        override fun isValid(): Boolean {
            return !TextUtils.isEmpty(id) && !TextUtils.isEmpty(symptomResponse) && !TextUtils.isEmpty(symptomValue)
        }

        fun toDBSeverity(idSymptom: String): DBSeverity {
            val model = DBSeverity()
            model.idSymptom = idSymptom
            model.idSeverity = id
            model.position = symptomValue.toIntOrNull() ?: 0
            model.symptomResponse = symptomResponse
            model.symptomValue = symptomValue
            return model
        }
    }

    fun dbPrograms(): ArrayList<DBProgram> {
        val programs = ArrayList<DBProgram>()
        for (program in data) {
            if (program.isValid()) {
                programs.add(program.toDBProgram())
            } else {
                Logger.e("Found an invalid program on ${javaClass.name}")
            }
        }
        return programs
    }

    fun dbSymptoms(): ArrayList<DBSymptom> {
        val symptoms = ArrayList<DBSymptom>()
        for (program in data) {
            if (program.isValid()) {
                for ((position, symptom) in program.symptoms.withIndex()) {
                    if (symptom.isValid()) {
                        symptoms.add(symptom.toDBSymptom(program.id, position))
                    } else {
                        Logger.e("Found an invalid symptom on ${javaClass.name}")
                    }
                }
            } else {
                Logger.e("Found an invalid program on ${javaClass.name}")
            }
        }
        return symptoms
    }

    fun dbOptions(): ArrayList<DBSeverity> {
        val options = ArrayList<DBSeverity>()
        for (program in data) {
            if (program.isValid()) {
                for (symptom in program.symptoms) {
                    if (symptom.isValid()) {
                        for (option in symptom.options) {
                            if (option.isValid()) {
                                options.add(option.toDBSeverity(symptom.symptomId))
                            } else {
                                Logger.e("Found an invalid option on ${javaClass.name}")
                            }
                        }
                    } else {
                        Logger.e("Found an invalid symptom on ${javaClass.name}")
                    }
                }
            } else {
                Logger.e("Found an invalid program on ${javaClass.name}")
            }
        }
        return options
    }
}
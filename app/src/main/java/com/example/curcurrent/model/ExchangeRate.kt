import com.google.gson.annotations.SerializedName


class ExchangeRate{
	data class Result (
		@SerializedName("base") val base : String,
		@SerializedName("rates") val rates : HashMap<String, Double>,
		@SerializedName("date") val date : String
	)
}

# lulo-bank-transaction-authorizer
 LULOBANK - Prueba técnica (Ing. Elkin Daza)

	Características:

		* Spring Boot 2.6.2
		* Java 11
		* JPA
		* Lombook
		* Gradle 7.3.2
		* JUnit 5
		* Mockito
		
	Apreciaciones:
	
		- Se utilizó SonarLint para asegurabilidad de código limpio, buenas prácticas en las rutinas y cero CodeSmells. El reporte quedó al 100% (0 Issues)
		- De acuerdo al tiempo para esta prueba se logró alcanzar un 63,2% de Coverage es Tests
		
	Guía de instalación (Usando Eclipse como ejemplo)

		Menú File -> Import
			Existing gradle project -> Next		
				Seleccionar ruta de la carpeta del repositorio descargado en Project root directory -> Next
					En "Import options" dejar seleccionado Gradle wrapper y presionar Finish.
								
		
		Al finalizar el importe del microservicio, puede realizar clic derecho sobre el proyecto y seleccionar Gradle -> Refresh gradle project
			Esto con el fin de descargar las dependencias si es necesario.
			  
		Correr el microservicio, haciendo clic derecho sobre el proyecto y hacer clic en la opción SpringBootApp
		
	Guía de pruebas:
	
		Al levantarse el servicio, se mostrará el siguiente mensaje:
		
		**********************************
		LISTENER TRANSACTION AUTHORIZER UP
		**********************************		
		
		Adicionalmente se le invita a que ingrese lineas json mediante el siguiente texto:
		
			ENTER ANY JSON FROM TRANSACTION (Or Write 'exit' and press Enter to finish)
			
		Este mensaje saldrá cada vez que procese una linea json, para que pueda finalizar la prueba cuando desee.
		
		A partir de allí puede insertar las lineas con las estructuras json definidas para las Cuentas y las Transacciones. A continuación, comparto
		un set de peticiones en la que se puede observar las distintas respuestas según las reglas de negocio para el microservicio:
		
		{"account": {"id": 1, "active-card": true, "available-limit": 100}}
		{"account": {"id": 1, "active-card": true, "available-limit": 100}}
		{"transaction": {"merchant": "Burger King","amount": 20, "time":"2019-02-13T10:00:00.000Z"}}
		{"transaction": {"merchant": "Burger King","amount": 20, "time":"2019-02-13T10:00:00.000Z"}}
		{"transaction": {"merchant": "Burger King 2","amount": 20, "time":"2019-02-13T10:00:00.000Z"}}
		{"transaction": {"merchant": "Burger King 3","amount": 20, "time":"2019-02-13T10:00:00.000Z"}}
		{"transaction": {"merchant": "Burger King 4","amount": 20, "time":"2019-02-13T10:00:00.000Z"}}
		{"transaction": {"merchant": "Burger King 4","amount": 50, "time":"2019-02-13T10:03:00.000Z"}}
		{"account": {"id": 2, "active-card": false, "available-limit": 100}}
		{"transaction": {"merchant": "Burger King 4","amount": 50, "time":"2019-02-13T10:03:00.000Z"}}
		
		Tras presionar Enter para lanzar todo este bloque de pruebas, las respuestas serían las siguientes:
		
		Response: {"account":{"active-card":true,"available-limit":100},"violations":[]}
		Response: {"account":{"active-card":true,"available-limit":100},"violations":["account-already-initialized"]}
		Response: {"account":{"active-card":true,"available-limit":80},"violations":[]}
		Response: {"account":{"active-card":true,"available-limit":80},"violations":["doubled-transaction"]}
		Response: {"account":{"active-card":true,"available-limit":60},"violations":[]}
		Response: {"account":{"active-card":true,"available-limit":40},"violations":[]}
		Response: {"account":{"active-card":true,"available-limit":40},"violations":["high-frequency-small-interval"]}
		Response: {"account":{"active-card":true,"available-limit":40},"violations":["insufficient-limit"]}		
		Response: {"account":{"active-card":false,"available-limit":100},"violations":[]}		
		Response: {"account":{"active-card":null,"available-limit":null},"violations":["card-not-active"]}
		
		
		En el momento que desee puede digitar la palabra EXIT y presionar Enter para detener el Listener de las transacciones. El sistema arrojará una respuesta
		como se observa a continuación:
		
		**************************************
		LISTENER TRANSACTION AUTHORIZER DOWNED
		**************************************

		Nota: Tener en cuenta que por linea solo debe hacer una estructura json, conforme el requerimiento de la prueba.
(ns ash-climate-files-to-html.core
    (:use [clojure.java.shell :only [sh]]
        [clojure.java.io :only [reader writer file copy]]
        [clojure.string :only [split capitalize]])
  (:import (java.io File)))

(def *out-file* "c:\\ashrae_to_fix\\test.html")
(def *workdir* "c:\\ashrae_to_fix\\fixed\\")
(def *file-list*  (seq (. (File. *workdir*) (listFiles))))
(def *zips* (filter #(. %1 endsWith "zip") (map #(. %1 getName) *file-list*)))
(def *countries* {:ABW "Aruba", :AFG "Afghanistan", :AGO "Angola",
                  :AIA "Anguilla", :ALA "Åland Islands", :ALB "Albania",
                  :AND "Andorra", :ANT "Dutch Antilles",
                  :ARE "United Arab Emirates", :ARG "Argentina", 
:ARM "Armenia"
:ASM 	"American Samoa"
:ATA 	"Antarctica"
:ATF 	"French Southern Territories"
:ATG 	"Antigua and Barbuda"
:AUS 	"Australia"
:AUT 	"Austria"
:AZE 	"Azerbaijan"
:BDI 	"Burundi"
:BEL 	"Belgium"
:BEN 	"Benin"
:BES 	"Bonaire, Sint Eustatius and Saba"
:BFA 	"Burkina Faso"
:BGD 	"Bangladesh"
:BGR 	"Bulgaria"
:BHR 	"Bahrain"
:BHS 	"Bahamas"
:BIH 	"Bosnia and Herzegovina"
:BLM 	"Saint Barthélemy"
:BLR 	"Belarus"
:BLZ 	"Belize"
:BMU 	"Bermuda"
:BOL 	"Bolivia, Plurinational State of"
:BRA 	"Brazil"
:BRB 	"Barbados"
:BRN 	"Brunei Darussalam"
:BTN 	"Bhutan"
:BVT 	"Bouvet Island"
:BWA 	"Botswana"
:CAF 	"Central African Republic"
:CAN 	"Canada"
:CCK 	"Cocos (Keeling) Islands"
:CHE 	"Switzerland"
:CHL 	"Chile"
:CHN 	"China"
:CIV 	"Côte d'Ivoire"
:CMR 	"Cameroon"
:COD 	"Congo, the Democratic Republic of the"
:COG 	"Congo"
:COK 	"Cook Islands"
:COL 	"Colombia"
:COM 	"Comoros"
:CPV 	"Cape Verde"
:CRI 	"Costa Rica"
:CUB 	"Cuba"
:CUW 	"Curaçao"
:CXR 	"Christmas Island"
:CYM 	"Cayman Islands"
:CYP 	"Cyprus"
:CZE 	"Czech Republic"
:DEU 	"Germany"
:DJI 	"Djibouti"
:DMA 	"Dominica"
:DNK 	"Denmark"
:DOM 	"Dominican Republic"
:DZA 	"Algeria"
:ECU 	"Ecuador"
:EGY 	"Egypt"
:ERI 	"Eritrea"
:ESH 	"Western Sahara"
:ESP 	"Spain"
:EST 	"Estonia"
:ETH 	"Ethiopia"
:FIN 	"Finland"
:FJI 	"Fiji"
:FLK 	"Falkland Islands (Malvinas)"
:FRA 	"France"
:FRO 	"Faroe Islands"
:FSM 	"Micronesia, Federated States of"
:GAB 	"Gabon"
:GBR 	"United Kingdom"
:GEO 	"Georgia"
:GGY 	"Guernsey"
:GHA 	"Ghana"
:GIB 	"Gibraltar"
:GIN 	"Guinea"
:GLP 	"Guadeloupe"
:GMB 	"Gambia"
:GNB 	"Guinea-Bissau"
:GNQ 	"Equatorial Guinea"
:GRC 	"Greece"
:GRD 	"Grenada"
:GRL 	"Greenland"
:GTM 	"Guatemala"
:GUF 	"French Guiana"
:GUM 	"Guam"
:GUY 	"Guyana"
:HKG 	"Hong Kong"
:HMD 	"Heard Island and McDonald Islands"
:HND 	"Honduras"
:HRV 	"Croatia"
:HTI 	"Haiti"
:HUN 	"Hungary"
:IDN 	"Indonesia"
:IMN 	"Isle of Man"
:IND 	"India"
:IOT 	"British Indian Ocean Territory"
:IRL 	"Ireland"
:IRN 	"Iran, Islamic Republic of"
:IRQ 	"Iraq"
:ISL 	"Iceland"
:ISR 	"Israel"
:ITA 	"Italy"
:JAM 	"Jamaica"
:JEY 	"Jersey"
:JOR 	"Jordan"
:JPN 	"Japan"
:KAZ 	"Kazakhstan"
:KEN 	"Kenya"
:KGZ 	"Kyrgyzstan"
:KHM 	"Cambodia"
:KIR 	"Kiribati"
:KNA 	"Saint Kitts and Nevis"
:KOR 	"Korea, Republic of"
:KWT 	"Kuwait"
:LAO 	"Lao People's Democratic Republic"
:LBN 	"Lebanon"
:LBR 	"Liberia"
:LBY 	"Libya"
:LCA 	"Saint Lucia"
:LIE 	"Liechtenstein"
:LKA 	"Sri Lanka"
:LSO 	"Lesotho"
:LTU 	"Lithuania"
:LUX 	"Luxembourg"
:LVA 	"Latvia"
:MAC 	"Macao"
:MAF 	"Saint Martin (French part)"
:MAR 	"Morocco"
:MCO 	"Monaco"
:MDA 	"Moldova, Republic of"
:MDG 	"Madagascar"
:MDV 	"Maldives"
:MEX 	"Mexico"
:MHL 	"Marshall Islands"
:MKD 	"Macedonia, the former Yugoslav Republic of"
:MLI 	"Mali"
:MLT 	"Malta"
:MMR 	"Myanmar"
:MNE 	"Montenegro"
:MNG 	"Mongolia"
:MNP 	"Northern Mariana Islands"
:MOZ 	"Mozambique"
:MRT 	"Mauritania"
:MSR 	"Montserrat"
:MTQ 	"Martinique"
:MUS 	"Mauritius"
:MWI 	"Malawi"
:MYS 	"Malaysia"
:MYT 	"Mayotte"
:NAM 	"Namibia"
:NCL 	"New Caledonia"
:NER 	"Niger"
:NFK 	"Norfolk Island"
:NGA 	"Nigeria"
:NIC 	"Nicaragua"
:NIU 	"Niue"	
:NLD 	"Netherlands"
:NOR 	"Norway"
:NPL 	"Nepal"
:NRU 	"Nauru"
:NZL 	"New Zealand"
:OMN 	"Oman"
:PAK 	"Pakistan"
:PAN 	"Panama"
:PCN 	"Pitcairn"
:PER 	"Peru"
:PHL 	"Philippines"
:PLW 	"Palau"
:PNG 	"Papua New Guinea"
:POL 	"Poland"
:PRI 	"Puerto Rico"
:PRK 	"Korea, Democratic People's Republic of"
:PRT 	"Portugal"
:PRY 	"Paraguay"
:PSE 	"Palestine, State of"
:PYF 	"French Polynesia"
:QAT 	"Qatar"
:REU 	"Réunion"
:ROU 	"Romania"
:RUS 	"Russian Federation"
:RWA 	"Rwanda"
:SAU 	"Saudi Arabia"
:SDN 	"Sudan"
:SEN 	"Senegal"
:SGP 	"Singapore"
:SGS 	"South Georgia and the South Sandwich Islands"
:SHN 	"Saint Helena, Ascension and Tristan da Cunha"
:SJM 	"Svalbard and Jan Mayen"
:SLB 	"Solomon Islands"
:SLE 	"Sierra Leone"
:SLV 	"El Salvador"
:SMR 	"San Marino"
:SOM 	"Somalia"
:SPM 	"Saint Pierre and Miquelon"
:SRB 	"Serbia"
:SSD 	"South Sudan"
:STP 	"Sao Tome and Principe"
:SUR 	"Suriname"
:SVK 	"Slovakia"
:SVN 	"Slovenia"
:SWE 	"Sweden"
:SWZ 	"Swaziland"
:SXM 	"Sint Maarten (Dutch part)"
:SYC 	"Seychelles"
:SYR 	"Syrian Arab Republic"
:TCA 	"Turks and Caicos Islands"
:TCD 	"Chad"
:TGO 	"Togo"
:THA 	"Thailand"
:TJK 	"Tajikistan"
:TKL 	"Tokelau"
:TKM 	"Turkmenistan"
:TLS 	"Timor-Leste"
:TON 	"Tonga"
:TTO 	"Trinidad and Tobago"
:TUN 	"Tunisia"
:TUR 	"Turkey"
:TUV 	"Tuvalu"
:TWN 	"Taiwan, Province of China"
:TZA 	"Tanzania, United Republic of"
:UGA 	"Uganda"
:UKR 	"Ukraine"
:UMI 	"United States Minor Outlying Islands"
:URY 	"Uruguay"
:USA 	"United States"
:UZB 	"Uzbekistan"
:VAT 	"Holy See (Vatican City State)"
:VCT 	"Saint Vincent and the Grenadines"
:VEN 	"Venezuela, Bolivarian Republic of"
:VGB 	"Virgin Islands, British"
:VIR 	"Virgin Islands, U.S."
:VNM 	"Viet Nam"
:VUT 	"Vanuatu"
:WLF 	"Wallis and Futuna"
:WSM 	"Samoa"
:YEM 	"Yemen"
:ZAF 	"South Africa"
:ZMB 	"Zambia"
                  :ZWE "Zimbabwe"})



(defn zip-again
  [file1 file2]
  (let [file-path *workdir*]
    (sh "c:\\program files\\7-zip\\7za.exe""a"(str file-path (remove-file-ending file1) ".zip") (str file-path file1) (str file-path file2))))


(defn write-new-country [country]
  (spit *out-file* 
        (str "</td>
</tr>
<tr>
<td>
<big><b><span class=\"bodytextTitle\">" country "</span></b></big>
</td>
</tr>
<tr>
<td VALIGN=TOP>
" )
        :append true))

(defn write-new-city [file city]
 (spit *out-file* 
       (str "<a href=\"" file "\" class=\"bodytext\">" city "</a><br>
"
            )
        :append true))
  


(write-out-file *zips*)

(defn write-out-file [files]
  (let [pres-count ((keyword (first (split (first files) #"_"))) *countries*)]
    (do
      (write-new-country pres-count)
      (loop [i 0 p-c pres-count]
        (if (= i (count files))
          "Done"
          (let [text (split (nth files i) #"_")
                country ((keyword (first text)) *countries*)
                city (apply str (map #(str (capitalize %1) " ") (split (second text) #"-")))]
            (if (= country p-c)
              (do
                (write-new-city (nth files i) city)
                (recur (inc i) p-c))
              (do
                (write-new-country country)
                (write-new-city (nth files i) city)
                (recur (inc i) country)))))))))
            
            
        (
(doc nth)
(defn do-all
  [directory]
  (let [file-list (seq (. (File. directory) (list)))]
    (doseq [i file-list]
      (let [ending (second (split i #"\."))
            file-name (remove-file-ending i)]
        (make-script i)
        (translate (str file-name "_IW2.ws"))
        (zip-again (str file-name "_IW2.prn") "weather.idm")))))


(do-all *workdir*)
((keyword "ABW") *countries*)
(apply str (map #(str (capitalize %1) " ") (split (second (split (first *iw2s*) #"_")) #"-")))
(doc get)	 
(let [file-list (seq (. (File. "c:\\programming\\translate\\") (list)))]
      (make-script (remove-file-ending (second file-list)))
      (translate))



(defn translate
  [file]
  (let [file-path *workdir*]
    (sh "c:\\Programming\\weatherstuff\\newold\\IceWeather.exe" (str file-path file))))


(defn make-script
  [file]
  (let [line1 (str "< " *workdir*)
	line2 (str "> " *workdir*)
	line3 "# ASHRAE_IW2"
	line4 (str " " (remove-file-ending file))]
  (spit (str *workdir* (remove-file-ending file)"_IW2.ws") (format "%s%n%s%n%s%n%s" line1 line2 line3 line4))))


(defn remove-file-ending
  [file]
  (first (split file #"\.")))


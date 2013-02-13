;;;
;;; Reads the file institutions.txt to get both the name of the institution
;;; and the license number.
;;; Don't forget to make a new haspdinst to use in the creation of the 
;;; installation program.
;;;

(load-file "c:\\users\\andreas\\clj-script\\make-edu-files.clj")

(ns make-edu.core
  (:use [clojure.java.shell :only [sh]]
        [clojure.java.io :only [reader writer]]
	[clojure.string :only [split]])
  (:import (java.io File)))

;; These should all be updated before making the files

(def *version* "'4.5.0'")
(def *infile* "ice45(tmp).exe")
(def *minim* "'4.0.0'")
(def *guid* "'FEFA385E-B32B-4B2C-B0D4-D08DD3D5EF75'")
(def *guid2* "'E1F0D528-DB09-4D53-8B02-E228538D4FA1'")
(def *workdir* "c:\\Users\\Andreas\\Desktop\\msitest\\IDAICEedu\\")



(defn read-lines [file]
  (with-open [r (reader file)]
    (doall (line-seq r))))

(defn write-lines [file s]
  (with-open [w (writer file)]
    (doseq [line s]
      (.write w  line))))


(defn file-exists? [file]
 (.exists (java.io.File. file)))

(defn read-institutions-and-codes
"Reads in institutions and codes from the infile. Format expected to
be lines with institutions and codes separated by a blank." 
  [infile]
  (let [listing (vec (map #(split % #" ")(read-lines infile)))]
 (map #(flatten (cons (str "IDAICEedu-" (first %)) [(rest %)])) listing)))


(defn write-wxs-files 
  [file-list]
  (let [file-names (vec (map first file-list))
	codes (vec (map second file-list))]
    (doall(for [i (range 0  (count file-names))]
      (let [file-name (str *workdir* (file-names i) ".wxs")]
	(spit file-name
	      (format "<?xml version='1.0' encoding='windows-1252'?>%n<Wix xmlns='http://schemas.microsoft.com/wix/2006/wi'>%n  <Product Name='IDA ICE 4' Id=%s UpgradeCode='F4A81CFC-518C-46AC-BA90-589B6347D18A'%n    Language='1033' Codepage='1252' Version=%s Manufacturer='EQUA'>%n%n%n%n    <Package Id='*' Keywords='Installer'%n      Description=\"Ice Installer\"%n      Comments='Bla' Manufacturer='EQUA'%n      InstallerVersion='100' Languages='1033' Compressed='yes' SummaryCodepage='1252' />%n%n <!-- Check for admin rights -->%n  <Condition Message=\"Administrator rights are required to install the IDA ICE 4. If you are using Windows UAC, please turn it off and try again.\">%n   Privileged%n  </Condition>%n    <Media Id='1' Cabinet='ice.cab' EmbedCab='yes' DiskPrompt=\"CD-ROM #1\" />%n%n    <Upgrade Id=\"F4A81CFC-518C-46AC-BA90-589B6347D18A\">%n%n      <UpgradeVersion Minimum=%s%n                      IncludeMinimum=\"no\"%n                      OnlyDetect=\"no\"%n                      Language=\"1033\"%n                    Property=\"DOWNGRADE\" />%n%n     <UpgradeVersion OnlyDetect='no'%n		      Minimum=%s%n                      IncludeMinimum=\"yes\"%n                      Maximum=%s%n                      IncludeMaximum=\"no\"%n                      RemoveFeatures='all'%n                      Language=\"1033\"%n       Property=\"UPGRADEFOUND\" />%n%n    </Upgrade>%n%n%n%n%n    <Property Id='DiskPrompt' Value=\"EQUA\" />%n%n    <Directory Id='TARGETDIR' Name='SourceDir'>%n	<Directory Id='ProgramFilesFolder' Name='PFiles'>%n      <Directory Id='INSTALLDIR' Name='Ida'>%n%n            <Component Id='MainExecutable' Guid=%s>%n              <File Id='ice.exe' Name='ice.exe' DiskId='1'%n               Source='%s' Vital='yes'>%n%n              </File>%n          
            <RegistryKey Action=\"createAndRemoveOnUninstall\" Key=\"Software\\Equa\\ida-ice\\4.0\" Root='HKLM'>%n        <RegistryValue Name=\"Ida_path\" Type=\"string\" Value='[INSTALLDIR]' />%n         
            <RegistryValue Name=\"tmplic\" Type=\"string\" Value='%s' />%n </RegistryKey>%n            <RemoveFolder Id='ProgramMenuDir' On='uninstall' />%n            </Component>%n%n           <Component Id='UtilExecutable' Guid=%s>%n              <File Id='runwise.exe' Name='runwise.exe' DiskId='1'%n                Source='runwise.exe' Vital='yes'>%n              </File>%n            </Component>%n      </Directory>%n	</Directory>%n    </Directory>%n%n    <Feature Id='Complete' Level='1'>%n      <ComponentRef Id='MainExecutable' />%n      <ComponentRef Id='UtilExecutable' />%n    </Feature>%n%n    <!-- Prevent downgrading -->%n%n    <CustomAction Id=\"PreventDowngrading\" Error=\"Newer version already installed.\" />%n    <CustomAction Id='LaunchFile' FileKey='ice.exe' ExeCommand='/S' Return='check' />%n <CustomAction Id='LaunchFile2' FileKey='runwise.exe' ExeCommand= '\"[INSTALLDIR]\\\"' Return='check' />%n     <CustomAction Id='LaunchFile3' FileKey='runwise.exe' ExeCommand= '\"[INSTALLDIR]\\\" \"%s\"' Return='check' />%n    <CustomAction Id='IsPrivileged' Error='Administrator rights are required to install the IDA ICE 4. If you are using Windows UAC, please turn it off and try again.' />%n%n    <InstallExecuteSequence>%n      <Custom Action=\"IsPrivileged\" Before='LaunchConditions'>NOT Privileged </Custom>%n      <Custom Action=\"PreventDowngrading\" After=\"FindRelatedProducts\">DOWNGRADE</Custom>%n  <RemoveExistingProducts After='InstallValidate' />%n      <Custom Action='LaunchFile' After='InstallFinalize'>  NOT Installed </Custom>%n  <Custom Action='LaunchFile2' After='InstallValidate'> Installed </Custom>%n     <Custom Action='LaunchFile3' After='LaunchFile'> NOT Installed </Custom>%n      <!--Custom Action='LaunchFile3' After='InstallFinalize'> NOT Installed </Custom-->%n    </InstallExecuteSequence>%n  </Product>%n</Wix>" 
               *guid* *version* *version* *minim* *version* *guid* *infile* (codes i) *guid2* *infile*)))))))



(defn add-language [file]
  (let [all-file (str *workdir* (first file) ".wxs")
	s (vec(read-lines all-file))]
    (write-lines all-file (flatten (list (subvec s 0 52) (format "             <File Id='language.exe' Name='language.exe' DiskId='1'%n                 Source= '%s' Vital='yes'> %n               </File>%n",  (nth file 2)) (subvec s 52 80) (format "    <CustomAction Id='LaunchFile4' FileKey='language.exe' ExeCommand= '/S' Return='check' /> "  ) (subvec s 80 88) (format "      <Custom Action='LaunchFile4' After='LaunchFile'> NOT Installed </Custom>%n  <Custom Action='LaunchFile3' After='LaunchFile4'> NOT Installed </Custom>%n") (subvec s 90 93))))))


(defn fix-files []
  (let [file-list (read-institutions-and-codes (str *workdir* "institutions.txt"))]
    (do
      (write-wxs-files file-list)
      (doseq [f file-list]
	(if (< 2 (count f)) 
	  (add-language f))
	(sh "candle.exe" (str (first f) ".wxs") :dir *workdir*)
	(Thread/sleep 500)
	(sh "light.exe" (str (first f) ".wixobj") :dir *workdir*)))))





(defn sign-files []
   "Signs the files with the sign.bat file"
   (let [file-list (read-institutions-and-codes (str *workdir* "institutions.txt"))
         files (map #(str *workdir* (first %) ".msi") file-list)]     
     (if (= (count files) (count (filter file-exists? files)))
       (for [f files]
	 (sh (str *workdir* "sign.bat") f :dir *workdir*))
       (do
	 (Thread/sleep 5000)
	 (sign-files)))))
          
(do
  (fix-files)
  (sign-files))

(range 0 (count [1 2 3]))


(defn read-contacts
"Reads in contacts from the infile.
The lines with institutions and contacts are separated by a blank." 
  [infile]
  (let [listing (vec (map #(split % #" ")(read-lines infile)))]
    (vec (map #(second %) listing))))



(defn write-mails-files
  "Writes the mail files later to be sent out."
  [vec]
  (doall (for [v vec]
	   (do
	   (let [file-name (str (first v) ".txt")]
	       (spit (str *workdir* file-name) (format "Exec: SEND%nAddr: %s%nSubj: New version of IDA ICE%n%n%nDear Sir/Madam,%n%nA new installation file of IDA ICE 4.5 is now available for download at http://www.equaonline.com/temp/%s.msi and will remain so for some days at least. This is a version with your personal license number in it.%n%nThere is a non-silent version available at: http://www.equa.se/deliv/ice45(tmp).exe%n%nYour license number is: %s%n%nIt is valid until 2013-05-31.%n%nIf you use the non-silent version, you will have to give the license number the first time you start the program. Just copy and paste it from above.%n%nThe following instructions are with regards to the educational/classroom version. Stand-alone installations use another document.%n%nA few things to think about before/during the installation:%n%n1. We recommend that you first uninstall a possible beta version of IDA ICE 4 and that you remove all old shortcuts and libraries that remain after you have uninstalled.%n2. If you want to retain an earlier version of IDA ICE, install IDA ICE 4 into a new folder. Be aware that you need to keep track of what shortcuts that lead to which version of the program.%n%nInstallation:%n%nInstall the IDA ICE 4 software on all machines you want to run IDA ICE 4 on on the network by using Group Policies and assigning the .msi-file to the computers. The default directory is c:\\Program Files\\IDA. This can be changed by specifying INSTALLDIR to be whatever you want using transforms (if using Group Policy) or by specifying INSTALLDIR=\"wanted\\path\" after msiexec /i IDAICE...msi if you are installing on each computer separately. The .msi-file needs to be run with administrative rights. If on Vista or Windows 7, the Windows UAC needs to be turned off, or the program run with elevated rights. Also deactivate any anti-virus programs on the computers.%n%nIf any problems occur during installation or activation, please contact ice.support@equa.se.%n%nBest regards,%n%nAndreas %n%nAndreas Edqvist Kissavos%nEQUA Simulation AB %nandreas.kissavos@equa.se%n+46 8 546 20 121 (phone)%n+46 8 546 20 101 (fax)%nRåsundavägen 100%n169 57 Solna, Sweden%nhttp://www.equa.se    " (v 2) (v 0) (v 1)))
	       (str *workdir* file-name))))))



(defn send-out-files 
  "Sends the files to mail addresses specified."
  [type]
  (let [file-list (read-institutions-and-codes (str *workdir* "institutions.txt"))
	file-list2 (read-contacts (str *workdir* "contacts.txt"))
	m (for [i (range 0 (count file-list))]
      (let [f (nth file-list i)]	
	[(first f) (second f) (nth file-list2 i)]))
	mails (if (= type "reg") (write-mails-reg m) (write-mails-files m))
        regs (when (= type "reg") (write-reg-files m))]    
    (doseq [mail mails]
      (sh "MailSupport.exe" mail :dir *workdir*))))

(send-out-files "real")

(defn write-reg-files [listing]
  (doseq [v listing]
    (let [file-name (str *workdir* (first v) "_lic.reg")]
          (spit file-name (format "Windows Registry Editor Version 5.00%n%n[HKEY_LOCAL_MACHINE\\SOFTWARE\\Equa\\ida-ice\\4.0]%n\"tmplic\"=\"%s\"" (second v))))))


(defn write-mails-reg [listing]
  (doall
    (for [v listing]
      (do
      (let [file-name (str (first v) ".txt")]
        (spit (str *workdir* file-name)  (format "Exec: SEND%nAddr: %s%nSubj: New IDA ICE license number%n%n%nDear Sir/Madam,%n%nYour IDA ICE Educational version needs a new license number.%nThe current license number expires on 2013-03-31.%n%nYour new license number:%n%nLicense number: %s %nValid to: 2013-05-31 %n%nHow do I update the licenses?%n%nTo update multiple clients:%n%n     1. Download a Windows Registry .reg file, containing your license number:%n       http://www.equaonline.com/temp/%s_lic.reg %n%n     2. Use Group Policy and the below command to distribute the .reg file to the clients in quiet mode: %n       regedit.exe /s %s_lic.reg.%n%n To update a single client maually:%n%n     1. Start IDA ICE on the client.%n     2. Choose Help > About IDA > Upgrade license.%n     3. Enter the license number above.%n%nFor questions, please contact us at ice.support@equa.se%n%nEQUA" (nth v 2) (second v) (first v) (first v)))
         (str *workdir* file-name))))))


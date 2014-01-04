(ns game.core)

(def level {:name "Level 1" :rooms [{:name "Room 1" :position [0 0] :walls [{:name "North" :opening "Yes" :secret "No" :gadget "Yes"} {:name "South" :opening "No" :secret "No" :gadget "No"} {:name "West" :opening "No" :secret "No"}] :monsters {}}]})


(defmulti start-values
  :ch-class)

(defrecord character [ch-class nm hp mana items right-hand left-hand strength vitality unused-skills skills])

(defmethod start-values
  "Warrior" [ch]
  (let [strength 15
        intelligence 8
        vitality 13]
  {:level 1 :strength strength :intelligence intelligence :vitality vitality :hp (* vitality 5) :mana 0 :unused-skills 5}))

(defmethod start-values
  "Magician" [ch]
  (let [strength 7
        intelligence 16
        vitality 9]
  {:level 1 :strength strength :intelligence intelligence :vitality vitality :hp (* vitality 3) :mana (* intelligence 5) :unused-skills 5}))

(defmethod start-values
  :default [ch]
  (let [strength 0
        intelligence 0
        vitality 0]
  {:level 1 :strength strength :intelligence intelligence :vitality vitality :hp (* vitality 5) :mana 0 :unused-skills 0}))

(defmulti gain-level
  :ch-class)

(defmethod gain-level
  "Warrior" [ch]
  (let [level (inc {:level ch})
        strength {:strength ch}
        intelligence {:intelligence ch}
        vitality {:vitality ch}
        hp {:hp ch}
        mana {:mana ch}
        unused-skills {:unused-skills ch}]
    {:level (inc level) :strength (+ strength 3) :intelligence (inc intelligence) :vitality (+ vitality 2) :hp (* 5 ())}))

(def test {:name "Level 1" :rooms [{:name "Room 1" :position [0 0]}]})

(:rooms test)
(filter (fn [x] (= "No" (:opening x))) (:walls (first (:rooms level))))
(let [P (character. "Warrior" "Ove" 123 0 [{:item-id "dagger-0" :name "Dagger" :damage 12}] "dagger-0" nil 12 120 0 [{:name "dagger" :level 2}])]
  (start-values P))

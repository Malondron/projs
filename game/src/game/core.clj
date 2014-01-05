(ns game.core)

(def level {:name "Level 1" :rooms [{:name "Room 1" :position [0 0] :walls [{:name "North" :opening "Yes" :secret "No" :gadget "Yes"} {:name "South" :opening "No" :secret "No" :gadget "No"} {:name "West" :opening "No" :secret "No"}] :monsters {}}]})

(def players (ref []))

(def weapon-list {:dagger-0 {:name "Small Dagger" :damage '(n-dice 6)}
                  :short-sword-0 {:name "Short Sword" :damage '(+ 2 (n-dice 6))}
                  :small-axe-0 {:name "Small Axe" :damage '(+ 2 (n-dice 8))}
                  :mace-0 {:name "Mace" :damage '(n-dice 8)}})

(def armour-list {:cloth-0 {:name "Soft Cloth" :defense 1}})

(def skill-list {:sword-and-maces {}})




(defmulti start-items
  :ch-class)

(defmethod start-items
  "Warrior" [ch]
  (let [weapons [:dagger-0
                 :short-sword-0
                 :small-axe-0
                 :mace-0]
        armour [:cloth-0]
        weapon-throw (n-dice 20)
        armour-throw (n-dice 6)
        weapon (cond
                 (>= 19 weapon-throw) (weapons 3)
                 (>= 17 weapon-throw) (weapons 2)
                 (>= 10 weapon-throw) (weapons 1)
                 true    (weapons 0))]
    [(weapon weapon-list) ((first armour) armour-list)]))


(start-items {:ch-class "Warrior"})

(defn n-dice [n]
   (inc (rand-int n)))

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
    {:level 1 :strength 3 :intelligence 1 :vitality 2 :hp (* 5 2) :unused-skills 3})

(def test {:name "Level 1" :rooms [{:name "Room 1" :position [0 0]}]})

(:rooms test)
(filter (fn [x] (= "No" (:opening x))) (:walls (first (:rooms level))))
(let [P (ref (character. "Warrior" "Ove" 0 0 [{:item-id "dagger-0" :name "Dagger" :damage 12}] "dagger-0" nil 0 0 0 [{:name "dagger" :level 2}]))
      starts (gain-level @P)]
  (dosync (commute P update-in [:strength] + (:strength starts)))
  @P)


(defn new-player []
  (let [nm "Kalle"
        ch-class "Warrior"
        ch (start-character nm ch-class)]
     (if (empty? (filter (fn [x] (= nm (:nm @x))) @players))
      (dosync
         (commute players conj ch))
       :error-duplicate-name)))

(new-player)
@players

(defn start-character [nm ch-class]
   (let [P (ref (character. ch-class nm 0 0 [{:item-id "dagger-0" :name "Dagger" :damage 12}] "dagger-0" nil 0 0 0 [{:name "dagger" :level 2}]))
         starts (start-values @P)
         start-items (start-items @P)]
     (dosync
      (for [val (vals starts)]
        (commute P update-in [val] + (val starts)))
      (commute P assoc-in [:items] start-items))
     P))

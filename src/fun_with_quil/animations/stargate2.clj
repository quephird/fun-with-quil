(ns fun-with-quil.animations.stargate2
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-streak []
  (let [coin-toss (rand-int 2)]
    {:x (q/random -1000 1000)
     :y 0
     :z (if (zero? coin-toss) -50 50)
     :h (if (zero? coin-toss) 0 220)
     :b (q/random 255)}))

(defn setup []
  (q/smooth)
  (q/color-mode :hsb)
  (q/no-stroke)
  (q/hint :enable-depth-sort)
  [(make-streak)])

(defn update [streaks]
  (->> streaks
    (concat (repeatedly 7 make-streak))
    (map (fn [s] (update-in s [:y] + 20)))
    (filter (fn [{y :y}] (< y 2000)))))

(defn draw [streaks]
  (let [fc (q/frame-count)
        h1 (mod fc 255)
        h2 (mod (+ 127 fc) 255)]
    (q/background 0)
    (q/camera 0 2000 0
              0 0 0
              0 0 -1)

    (doseq [{:keys [x y z h b]} streaks]
      (if (= z 50)
        (q/fill h1 255 b)
        (q/fill h2 255 b))
      (q/push-matrix)
      (q/translate x y z)
      (q/rect 0 0 75 200)
      (q/pop-matrix))))

(q/defsketch stargate2
  :title      "stargate 2"
  :size       :fullscreen
  :features  [:present]
  :renderer   :p3d
  :setup      setup
  :update     update
  :draw       draw
  :middleware [m/fun-mode])

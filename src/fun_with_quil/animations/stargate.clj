(ns fun-with-quil.animations.stargate
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-starburst []
  {:x (q/random -1000 1000)
   :y -3000
   :z (if (zero? (rand-int 2)) -100 100)
   :r 10
   :h (q/random 255)})

(defn setup []
  (q/smooth)
  (q/color-mode :hsb)
  (q/no-stroke)
  [(make-starburst)])

(defn update [starbursts]
  (let [new-starburst (if (< (q/random 1) 0.05) [(make-starburst)])]
    (->> starbursts
      (concat new-starburst)
      (map (fn [sb] (-> sb
                      (update-in [:y] + 20)
                      (update-in [:r] + 10))))
      (filter (fn [{y :y}] (< y 2000))))))

(defn draw [starbursts]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/background 0)
    (q/camera 0 2000 0
              0 0 0
              0 0 -1)

    (doseq [{:keys [x y z r h]} starbursts]
      (q/fill h 255 255)
      (q/push-matrix)
      (q/translate x y z)
      (dotimes [i 12]
        (q/push-matrix)
        (q/translate r 0 0)
        (q/rect 0 0 (* 0.1 r) (* 0.7 r))
        (q/pop-matrix)
        (q/rotate-z (/ q/PI 12))
        )
      (q/pop-matrix))))

(q/defsketch stargate
  :size       [1440 800]
  :title      "stargate"
  :renderer   :p3d
  :setup      setup
  :update     update
  :draw       draw
  :middleware [m/fun-mode])

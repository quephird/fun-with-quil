(ns fun-with-quil.animations.tunnel
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-wall []
  {:side (rand-int 4)
   :y -3000})

(defn setup []
  (q/smooth)
  (q/color-mode :hsb)
  (q/rect-mode :center)
  (q/no-stroke)
  [(make-wall)])

(defn update [tunnel]
  (let [new-wall (if (< (q/random 1) 0.3) [(make-wall)])]
    (->> tunnel
      (concat new-wall)
      (map (fn [w] (update-in w [:y] + 50)))
      (filter (fn [{y :y}] (< y 2000))))))

(defn draw [tunnel]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/background 0)
    (q/camera -275 2000 -275
              0 0 0
              0 0 -1)

    (doseq [{:keys [side y]} tunnel]
      (q/fill 200 255 255)
      (q/push-matrix)
      (q/rotate-y (* q/HALF-PI side))
      (q/translate 300 y 300)
      (q/rect -300 -300 600 200)
      (q/pop-matrix)
      )))

(q/defsketch tunnel
  :size       :fullscreen
  :features  [:present]
  :title      "tunnel"
  :renderer   :p3d
  :setup      setup
  :update     update
  :draw       draw
  :middleware [m/fun-mode])

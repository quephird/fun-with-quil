(ns fun-with-quil.animations.worms
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-worms [max-x max-y n]
  (for [i (range n)]
    {:x (q/random max-x)
     :y (q/random max-y)
     :r (q/random 200 300)
     :h (q/random 0 40)
     :dθ (q/random 0 q/TWO-PI)
     :sw (q/random 10 80)
     :θ (q/random q/HALF-PI q/PI)}))

(defn setup []
  (let [w  (q/width)
        h  (q/height)]
    (q/smooth)
    (q/color-mode :hsb)
    (make-worms w h 40)))

(defn draw [worms]
  (let [fc (q/frame-count)
        dϕ (* fc 0.1)]
    (q/background 0)
    (q/no-fill)
    (doseq [{:keys [x y r h θ dθ sw]} worms]
      (q/stroke h 255 255)
      (q/stroke-weight sw)
      (q/arc x y r r (+ dϕ dθ) (+ dϕ dθ θ)))
    )
  )

(q/defsketch worms
  :title "worms"
  :setup setup
  :draw draw
  :size [1440 800]
  :middleware [m/fun-mode])

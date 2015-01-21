(ns fun-with-quil.animations.worms
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-worms [max-x max-y n]
  (for [i (range n)]
    {:x (q/random max-x)
     :y (q/random max-y)
     :dx (q/random -10 10)
     :dy (q/random -10 10)
     :r (q/random 100 200)
     :h (q/random 0 40)
     :dθ (q/random 0 q/TWO-PI)
     :sw (q/random 10 60)
     :θ (q/random q/HALF-PI q/PI)}))

(defn setup []
  (let [w  (q/width)
        h  (q/height)]
    (q/smooth)
    (q/color-mode :hsb)
    {:worms (make-worms w h 70) :w w :h h}))

(defn update [{worms :worms w :w h :h :as state}]
  (let  [new-worms (->> worms
                     (map (fn [{dx :dx :as worm}] (update-in worm [:x] + dx)))
                     (map (fn [{dy :dy :as worm}] (update-in worm [:y] + dy)))
                     (map (fn [{x :x dx :dx :as worm}] (assoc-in worm [:dx] (if (or (>= x w) (<= x 0)) (- dx) dx))))
                     (map (fn [{y :y dy :dy :as worm}] (assoc-in worm [:dy] (if (or (>= y h) (<= y 0)) (- dy) dy)))))]
    (assoc-in state [:worms] new-worms)))

(defn draw [{worms :worms :as state}]
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
  :size [1200 800]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])

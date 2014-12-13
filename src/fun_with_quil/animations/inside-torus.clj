(ns hello-quil.animations.inside-torus
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn surface [θ φ]
  (let [big-r 200 little-r 75]
    [(* (q/cos θ) (+ big-r (* little-r (q/cos φ))))
     (* (q/sin θ) (+ big-r (* little-r (q/cos φ))))
     (* little-r (q/sin φ))]))

(defn setup []
  (q/no-stroke)
  (q/smooth)
  (q/color-mode :hsb)
  (q/stroke-cap :round)
  (q/camera 600 600 0 600 0 0 0.0 0.0 -1.0)
  {:dθ 0.0 :dφ 0.0 :dψ 0.0})

(defn update [state]
  (-> state
    (update-in [:dθ] (fn [θ] (+ θ 0.0078)))
    (update-in [:dφ] (fn [φ] (+ φ 0.0078)))))

(defn draw [state]
  (let [num-circles 50
        num-streaks 40
        num-points  7
        w           (q/width)
        h           (q/height)
        dθ          (:dθ state)
        dφ          (:dφ state)]
    (q/background 0)

    (q/translate (* w 0.5) (* h 0.5))
    (q/stroke-weight 5)

    (dotimes [i num-circles]
      (dotimes [j num-streaks]
        (dotimes [k num-points]
          (let [θ (+ dθ (* i (/ (* 2 q/PI) num-circles)) (* k 0.01))
                φ (+ dφ (* j (/ (* 2 q/PI) num-streaks)))
                [x y z] (surface θ φ)
                h (q/map-range (rem φ (* 2 q/PI)) 0 (* 2 q/PI) 255 0)
                b (q/map-range k 0 (dec num-points) 0 255)]
            (q/stroke h 255 b)
            (q/point x y z)))))))

(q/defsketch inside-torus
  :title "inside torus"
  :setup setup
  :size [800 800]
  :renderer :p3d
  :draw draw
  :update update
  :middleware [m/fun-mode]
 )

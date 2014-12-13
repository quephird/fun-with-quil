(ns hello-quil.animations.klein
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn surface [θ φ]
  (let [a 200 b 125 m 1 n 2]
    [(* (+ a (* (q/cos (* n θ 0.5)) (q/sin φ)) (* (- b) (q/sin (* n θ 0.5)) (q/sin (* 2 φ)))) (q/cos (* m θ 0.5)))
     (* (+ a (* (q/cos (* n θ 0.5)) (q/sin φ)) (* (- b) (q/sin (* n θ 0.5)) (q/sin (* 2 φ)))) (q/sin (* m θ 0.5)))
     (* 0.5 b (q/sin (* n θ)) (q/sin φ) (q/sin (* 2 φ)))]))

(defn setup []
  (q/no-stroke)
  (q/smooth)
  (q/color-mode :hsb)
  (q/stroke-cap :round)
  {:dθ 0.0 :dφ 0.0 :dψ 0.0})

(defn update [state]
  (-> state
    (update-in [:dθ] (fn [θ] (+ θ 0.0078)))
    (update-in [:dφ] (fn [φ] (+ φ 0.0078)))
    (update-in [:dψ] (fn [ψ] (+ ψ 0.0314)))))

(defn draw [state]
  (let [num-circles 50
        num-streaks 40
        num-points  7
        w           (q/width)
        h           (q/height)
        dθ          (:dθ state)
        dφ          (:dφ state)
        dψ          (:dψ state)
       ]
    (q/background 0)

    (q/text-size 20)
    (q/fill 0 0 255)
    (q/text "x(θ,φ) = (a + cos θ sin φ - b sin θ sin 2φ) cos(θ/2)" 50 50)
    (q/text "y(θ,φ) = (a + cos θ sin φ - b sin θ sin 2φ) sin(θ/2)" 50 75)
    (q/text "z(θ,φ) = ½b sin θ sin φ sin 2φ" 50 100)

    (q/translate (* w 0.5) (* h 0.5))
    (q/rotate-x (+ (* q/PI 0.5) dψ))
    (q/stroke-weight 5)


    (dotimes [i num-circles]
      (dotimes [j num-streaks]
        (dotimes [k num-points]
          (let [θ (+ dθ (* i (/ (* 4 q/PI) num-circles)) (* k 0.03))
                φ (+ dφ (* j (/ (* 2 q/PI) num-streaks)))
                [x y z] (surface θ φ)
                h (q/map-range (rem φ (* 2 q/PI)) 0 (* 2 q/PI) 255 0)
                b (q/map-range k 0 (dec num-points) 0 255)]
            (q/stroke h 255 b)
            (q/point x y z)))))))

(q/defsketch klein
  :title "gray's klein bottle"
  :setup setup
  :size [800 800]
  :renderer :p3d
  :draw draw
  :update update
  :middleware [m/fun-mode])

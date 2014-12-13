(ns fun-with-quil.animations.bowtie
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [gil.core :as g]))

(defn surface [θ φ]
  [(* 125 (/ (q/sin θ) (+ (q/sqrt 2) (q/sin φ))))
   (* 125 (/ (q/sin θ) (+ (q/sqrt 2) (q/cos φ))))
   (* 125 (/ (q/cos θ) (+ 1 (q/sqrt 2))))]
  )

(defn setup []
  (q/no-stroke)
  (q/smooth)
  (q/color-mode :hsb)
  (q/stroke-cap :round)
  {:dθ 0.0 :dφ 0.0 :dψ 0.0}
  )

(defn update [state]
  (-> state
    (update-in [:dθ] (fn [θ] (+ θ 0.0314)))
    (update-in [:dφ] (fn [φ] (+ φ 0.0078)))
    (update-in [:dψ] (fn [ψ] (+ ψ 0.0157))))
  )

(defn draw [state]
  (let [num-circles 20
        num-streaks 20
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
    (q/text "x(θ,φ) = a sin(θ)/(√2 + sin(φ))" 50 50)
    (q/text "y(θ,φ) = a sin(θ)/(√2 + cos(φ))" 50 75)
    (q/text "z(θ,φ) = a cos(θ)/(√2 + 1)" 50 100)

    (q/translate (* w 0.5) (* h 0.5))
    (q/rotate-x (+ (* q/PI 0.5) dψ))
;    (q/rotate-y (* q/PI 0.16))
    (q/stroke-weight 5)


    (dotimes [i num-circles]
      (dotimes [j num-streaks]
        (dotimes [k num-points]
          (let [θ (+ dθ (* i (/ q/TWO-PI num-circles)))
                φ (+ dφ (* j (/ q/TWO-PI num-streaks)) (* k 0.03))
                [x y z] (surface θ φ)
                h (q/map-range (rem φ q/TWO-PI) 0 q/TWO-PI 255 0)
                b (q/map-range k 0 (dec num-points) 0 255)]
            (q/stroke h 255 b)
            (q/point x y z)
            )
          )
        )
      )

;    (g/save-animation "bowtie.gif" 100 10)
    )
  )

(q/defsketch bowtie
  :title "bowtie"
  :setup setup
  :size [800 800]
  :renderer :p3d
  :draw draw
  :update update
  :middleware [m/fun-mode]
 )

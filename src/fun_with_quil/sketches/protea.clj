(ns fun-with-quil.protea
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

; Naming of flower parts taken from this article:
;
;   http://www.microscopy-uk.org.uk/mag/indexmag.html?http://www.microscopy-uk.org.uk/mag/artdec05/bjprotea.html

; TODO: Probably need some sort of threadlocal to define
;         this global setting; look at source for
;         sphereDetail().
;(defn cone-detail [n])

; TODO: Technically, I ought to draw the base as well,
;         although it's not needed in this particular sketch.
(defn cone [r h]
  (let [n 20]
;    (q/ellipse 0 0 r r)
    (q/push-matrix)
    (let [t (/ q/TWO-PI n)]
      (dotimes [i n]
        (q/begin-shape :triangles)
        (q/vertex r 0 0)
        (q/vertex (* r (q/cos t)) (* r (q/sin t)) 0)
        (q/vertex 0 0 h)
        (q/end-shape)
        (q/rotate (- t))))
     (q/pop-matrix)))

(defn make-pistils []
  (into []
    (for [θ (range 0 360 5)]
      (let [r (q/random 90 110)
            x (* r (q/cos (q/radians θ)))
            y (* r (q/sin (q/radians θ)))
            z r]
        [x y z]))))

(defn draw-pistils [pistils]
    (doseq [[x2 y2 z2] pistils]
      (q/no-fill)
      (q/stroke 255 255 0)
      (q/stroke-weight 5)
      (q/bezier 0 0 0
                x2 y2 0
                x2 y2 (* 0.5 z2)
                x2 y2 z2)
      (q/no-stroke)
      (q/fill 255 255 0)
      (q/push-matrix)
      (q/translate x2 y2 z2)
      (q/sphere 3)
      (cone 3 10)
      (q/pop-matrix)))

(defn draw-stamens []
  (dotimes [_ 20]
  (let [r                (q/random 20 40)
        θ                (q/random q/TWO-PI)
        bend-factor      (q/random 1.5 3)
        [x1 y1 z1]       [(q/random -50 50) (q/random -50 50) (q/random 10)]
        [cpx1 cpy1 cpz1] [(+ x1 (* (- bend-factor (q/cos θ)))) (+ y1 (* (- bend-factor (q/sin θ)))) (+ z1 100)]
        [x2 y2 z2] [(+ x1 (* r (q/cos θ))) (+ y1 (* r (q/sin θ))) (+ z1 50)]
        [cpx2 cpy2 cpz2] [(+ x2 (* bend-factor (q/cos θ))) (+ y2 (* bend-factor (q/sin θ))) (+ z1 100)]
        ]
      (q/no-fill)
      (dotimes [i 7]
        (if (even? i)
          (do
            (q/stroke 255 0 0)
            (q/stroke-weight 8)
            (q/bezier (+ x1 (* i 3)) y1 z1
                      (+ cpx1 (* i 3)) cpy1 cpz1
                      (+ cpx2 (* i 3)) cpy2 cpz2
                      (+ x2 (* i 3)) y2 z2))
          (do
            (q/stroke 255 255 255)
            (q/stroke-weight 3)
            (q/bezier (+ x1 (* i 3)) y1 z1
                      (+ cpx1 (* i 3)) cpy1 cpz1
                      (+ cpx2 (* i 3)) cpy2 cpz2
                      (+ x2 (* i 3)) y2 z2))
          )
        )
      )
    )
  )


(defn setup []
  (q/smooth)
  (q/no-loop)
  {:pistils (make-pistils)}
  )

; TODO: Try to introduce a light to give a better 3D effect.
(defn draw [state]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        pistils (state :pistils)]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h) 0)
    (q/camera (* -0.3 w) 0 (* 0.3 h)
              0 0 0
              0 0 -1)
;    (q/point-light 0 0 0
;                   100 100 100)
;    (cone 100 200)
    (q/rotate-x (q/radians 15))
    (draw-pistils pistils)
    (draw-stamens)

    (q/save "protea.png")))

(q/defsketch protea
  :title "protea"
  :renderer :p3d
  :setup setup
  :draw draw
  :size [800 800]
  :middleware [m/fun-mode])

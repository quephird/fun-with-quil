(ns fun-with-quil.sketches.agate
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-points []
  (let [n (int (q/random 8 10))]
    (for [i (range n)]
      [(+ (q/random -50 50) (* 400 (q/cos (* i (/ q/TWO-PI n)))))
       (+ (q/random -50 50) (* 300 (q/sin (* i (/ q/TWO-PI n)))))])))

(defn setup []
  (q/smooth)
  (q/no-loop)
  (q/no-fill)
  (q/color-mode :hsb)
  (make-points)
  )

(defn draw [points]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        n  100
        bs  (into [] (repeatedly n (fn [] (q/random 255))))
        points' (concat (rest points) [(first points)])
        pairs   (map vector points points')]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))
    (q/rotate (q/radians (q/random -20 20)))
    (q/stroke-weight 6)
    (q/stroke 0 255 0)

    ; Computing arc centers algebraically is prohibitively difficult
    ; so I decided to resort to a trigonometric algorithm.
    ;
    ; The strategy is: given a set of points somewhat randomly
    ; generated around an ellipse, we first pair adjacent ones up.
    ; Then for each pair, we need to compute the point from which
    ; to generate concentric arcs by fixing a distance between
    ; it and each of the points in the pair. From there, we need
    ; to compute the angular width subtended by the pair onto
    ; the arc center. We also need to determine the angle of rotation
    ; between the line joining the arc center and the mindpoint to
    ; orient the arcs properly.
    ;
    ;
    ;     first point
    ;       (x1,y1)
    ;          *----__
    ;                 `---__
    ;                       `--_
    ;                           `--_
    ;                               `_
    ;               (xm,ym)           \
    ;               midpoint *          \
    ;                                    \
    ;                                     \
    ;        (xc,yc) *                    |
    ;       arc center                    |
    ;                                     |
    ;                                     *
    ;                                 (x2,y2)
    ;                               second point
    ;
    ;

    (doseq [i (range (dec n) 0 -1)]
      ; These computations need to be moved out of here
      ; and done once only but for now it works.
      (doseq [[[x1 y1] [x2 y2]] pairs]
        (let [dx  (- x2 x1)                            ; Compute deltas to save some work below
              dy  (- y2 y1)
              l   (Math/sqrt (+ (*  dx dx) (* dy dy))) ; Compute the distance between the two points
              r   (* 0.7 l)                            ; Arbitrary choice for fixing the arc center
              d   (Math/sqrt (- (* r r) (* 0.25 l l))) ; Compute distance between arc center and midpoint
              xm  (+ x1 (* 0.5 dx))                    ; Compute coordinates of midpoint
              ym  (+ y1 (* 0.5 dy))
              θ'  (if (< dx 0)                         ; Compute angle of rotation for drawing arcs
                    (q/acos (/ dy l))
                    (+ q/PI (q/acos (/ (- dy) l))))
              dx' (* d (q/cos θ'))                     ; Compute offsets from midpoint
              dy' (* d (q/sin θ'))
              xc  (- xm dx')                           ; Compute coordinates of arc center
              yc  (- ym dy')
              ϕ   (q/atan (/ l d 2))                   ; Compute angular width of arc
              d' (+ (* i 10) (* 2 r))]                 ; Compute radius of each arc
          (q/push-matrix)
          (q/translate xc yc)
          (q/rotate θ')
          (q/stroke 10 255 (bs i))
          (q/arc 0 0 d' d' (- ϕ) ϕ)
          (q/pop-matrix)
          ))))
    (q/save "agate.png"))

(q/defsketch agate
  :title "agate"
  :setup setup
  :draw draw
  :size [1920 1080]
  :middleware [m/fun-mode])

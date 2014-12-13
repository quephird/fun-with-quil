(ns sempervivium
  (:use quil.core))

(def screen-w 800)
(def screen-h 800)

(def rosette-count 15)
(def leaf-color [110 148 73])
(def leaf-edge-color [220 255 146])

(defn setup []
  (smooth)
  (background 0)
  (no-loop))

; This is a bit hackish; I currently do not know how to evenly
; distribute points across a Bezier curve, so I approximate the
; the curve length below and distribute the points linearly.
(defn- draw-hairs [x1 y1 cpx1 cpy1 cpx2 cpy2 x2 y2]
  (stroke 255)
  (let [dx (- x2 x1)
        dy (- y2 y1)
        m (/ dy dx)
        hair-dy -7
        hair-dx (if (> dy 0) 7 -7)
        l (sqrt (+ (* dx dx) (* dy dy)))
        hair-count (/ l 4)]
    (doseq [i (range hair-count)]
      (let [x (bezier-point x1 cpx1 cpx2 x2 (/ i hair-count))
            y (bezier-point y1 cpy1 cpy2 y2 (/ i hair-count))]
        (line x y (+ x hair-dx) (+ y hair-dy)))))
  (no-stroke))

(defn- draw-leaf [idx]
  ; Draw leaf shape and fill it; radial position is dependent on rosette index
  ; Color of leaf is also dependent on index; darker on the outside, lighter
  ; in the center.
  (push-matrix)

  (let [c (map-range idx rosette-count 1 0.2 1.0)
        leaf-width (map-range idx rosette-count 1 300 30)
        leaf-height leaf-width]

    (translate 0 (* idx idx -1.7))
    (stroke-weight 2)
    (apply stroke leaf-edge-color)
    (apply fill (map #(* % c) leaf-color))
    (begin-shape)

    (vertex (* leaf-width -0.5) 0)
    (bezier-vertex (* leaf-width -0.5) (* -0.9 leaf-height)
                  0 (* -0.9 leaf-height)
                  0 (* -1 leaf-height))
    (bezier-vertex 0 (* -0.9 leaf-height)
                  (* leaf-width 0.5) (* -0.9 leaf-height)
                  (* leaf-width 0.5) 0)
    (end-shape)
    (stroke-weight 1)

    (draw-hairs (* leaf-width -0.5) 0
                (* leaf-width -0.5) (* -0.9 leaf-height)
                0 (* -0.9 leaf-height)
                0 (* -1 leaf-height))
    (draw-hairs 0 (* -1 leaf-height)
                0 (* -0.9 leaf-height)
                (* leaf-width 0.5) (* -0.9 leaf-height)
                (* leaf-width 0.5) 0))
    (pop-matrix))

(defn- draw-rosette [idx]
  (let [leaf-count (int (+ 3 (* idx idx 0.05)))]
    (doseq [_ (range leaf-count)]
      (draw-leaf idx)
      (rotate (radians (/ 360 leaf-count))))))

(defn draw []
  (translate (/ screen-w 2) (/ screen-h 2))
  (doseq [idx (reverse (range rosette-count))]
    (rotate (radians (random 360)))
    (draw-rosette idx))
  (save "sempervivium.png"))

(defsketch main
  :title "sempervivium"
  :setup setup
  :draw draw
  :size [screen-w screen-h])

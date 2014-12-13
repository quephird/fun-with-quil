(ns fun-with-quil.animations.beziers
  (:use quil.core))

(def screen-w 1000)
(def screen-h 1000)

(defn setup []
  (smooth)
  (color-mode :hsb)
  )

(defn draw []
  (let [center-of-screen   [(* screen-w 0.5) (* screen-h 0.5)]
        fc                 (frame-count)
        lobe-curve-width   2
        lobe-curve-color   255
        lobe-count         40
        star-points        7
        lobe-color         [(rem fc 255) 220 220 50]
        lobe-length-max    (* screen-w 0.45)
        starting-angle     (radians (* fc 0.5))
        a                  (* lobe-length-max 0.1)
        b                  (* star-points (/ 360 lobe-count))
        c                  (* fc 5)
        d                  (* lobe-length-max 0.9)]
    (background 0)
    (stroke-weight lobe-curve-width)
    (stroke lobe-curve-color)
    (apply translate center-of-screen)
    (apply fill lobe-color)
    (rotate starting-angle)
    (doseq [i (range lobe-count)]
      (let [[x1 y1]               [0 0]
            [x2 y2]               [(+ (* a (sin (radians (+ (* b i) c)))) d) 0]
            [cpx1 cpy1]           [(* x2 0.25) (* screen-w 0.1)]
            [cpx2 cpy2]           [(* x2 0.95) (* screen-w -0.2)]
            angle-between-lobes   (radians (/ 360 lobe-count))]
        (bezier x1 y1 cpx1 cpy1 cpx2 cpy2 x2 y2)
        (rotate angle-between-lobes)
        )
      )
    )
  )

(sketch
  :title "beziers"
  :setup setup
  :draw draw
  :size [screen-w screen-h])

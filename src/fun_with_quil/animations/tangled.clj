(ns fun-with-quil.animations.tangled
  (:use quil.core))

(def screen-w 1000)
(def screen-h 1000)

(def t (atom 0.0))

(defn setup []
  (smooth)
  (no-fill)
  (stroke-weight 7)
  (perspective (/ PI 2.3) 1 100 (+ 100 (* 2 screen-w))))

(defn draw []
  (let [num-segments  700
        radius        (* 0.5 (min screen-w screen-h))
        samples-per-frame 32
        num-frames    120
        tw1  (* 0.5 (- 1.0 (cos (* TWO-PI @t))))
        tw2  (+ (* 0.5 tw1) (* 1.5 tw1 tw1) (* -1.0 tw1 tw1 tw1))
        tw   (* 3.0 PI tw2)
        bg-color [32 30 30]]
    (reset! t (map-range (dec (frame-count)) 0 num-frames 0.0 1.0))
    (color-mode :rgb 255)
    (apply background bg-color)
    (push-matrix)
    (translate (/ screen-w 2) (/ screen-h 2))

    (color-mode :hsb 255)
    (begin-shape)
    (doseq [i (range num-segments)]
      (let [th (/ (* i TWO-PI) num-segments)
            ph (* tw (cos (* 3 th)))
            x  (* radius (cos th))
            y  (* radius (sin th) (cos ph))
            z  (* radius (sin th) (sin ph))
            xx (- (* x (cos (* PI @t))) (* z (sin (* PI @t))))
            zz (- (- (* x (sin (* PI @t)))) (* z (cos (* PI @t))))
            min-h 140
            max-h 180
            h  (map-range zz (- radius) radius min-h max-h)]
        (stroke h 255 255)
        (vertex xx y zz)))
    (end-shape :close)
    (pop-matrix)))

(sketch
  :title "tangled"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [screen-w screen-h])

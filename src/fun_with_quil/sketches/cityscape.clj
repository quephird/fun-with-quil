(ns fun-with-quil.sketches.cityscape
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m])
  (:import [processing.core PGraphics]))

(defn make-windows [n]
  (into [] (repeatedly n (fn [] (rand-int 3)))))

(defn- make-skyscraper []
  (let [storeys (+ 10 (rand-int 20))
        windows-per-floor (+ 4 (rand-int 4))
        windows (make-windows (* storeys windows-per-floor))]
    {:brick-color (rand-int 50)
     :storeys storeys
     :windows-per-floor windows-per-floor
     :windows windows
     :lights-on-top (rand-int 3)
     :light-color ([[200 0 0] [200 200 0] [0 200 0] [200 0 200]](rand-int 4))})
    )

; Give x,y coords for each skyscraper; there should be gaps between buildings
(defn- make-skyscrapers [n]
  (into [] (for [i (range n)] [i (make-skyscraper)])))

(defn setup []
  (q/smooth)
  (q/no-stroke)
  (q/background 0)
  (q/no-loop))

(defn stars [sky-w sky-h]
  (let [star-color [255 240 210]]
    (apply q/stroke star-color)
    (q/stroke-weight 4)
    (dotimes [i 5]
      (let [star-x (q/random sky-w)
            star-y (q/random (* 0.7 sky-h))]
        (q/point star-x star-y)))))

(defn moon [sky-w sky-h]
  (let [moon-x (q/random sky-w)
        moon-y (q/random (* 0.5 sky-h))
        moon-d 100
        moon-color [255 240 210]
        shadow-color [140 120 100]]
    (q/no-stroke)
    (apply q/fill moon-color)
    (q/ellipse moon-x moon-y 100 100)

    (q/push-matrix)
    (q/translate moon-x moon-y)
    (q/rotate (q/radians -20))
    (apply q/fill shadow-color)
    (q/arc 0 0 100 100 (q/radians 100) (q/radians 260))
    (q/arc (* -0.2 moon-d) 0 moon-d moon-d (q/radians -80) (q/radians 80))
    (q/pop-matrix)))

(defn sunset [sky-w sky-h]
  (let [top-color    [0 0 127]
        bottom-color [127 80 0]]
    (q/no-stroke)
    (q/begin-shape :quads)
    (apply q/fill top-color)
    (q/vertex 0 0)
    (q/vertex sky-w 0)
    (apply q/fill bottom-color)
    (q/vertex sky-w sky-h)
    (q/vertex 0 sky-h)
    (q/end-shape))

  ; Maybe simulate clouds?
  )

; Render lights
; Rippling effect?
; Combine draw-skyscraper and draw-reflection somehow
(defn- draw-skyscraper [brick-color storeys windows-per-floor windows]
  (q/no-stroke)
  (q/fill brick-color)
  (q/rect 0 0 (* windows-per-floor 10) (* storeys -10))

  (q/push-matrix)
  (q/fill 255 255 200)
  (dotimes [s storeys]
    (q/push-matrix)
    (dotimes [w windows-per-floor]
      (if (zero? (windows (+ w (* s windows-per-floor))))
        (q/rect 0 0 5 -5))
      (q/translate 10 0))
    (q/pop-matrix)
    (q/translate 0 -10))
  (q/pop-matrix)
  )

(defn- draw-skyscrapers [skyscrapers]
  (q/push-matrix)
  (doseq [[_ {:keys [brick-color storeys windows-per-floor windows]}] skyscrapers]
    (draw-skyscraper brick-color storeys windows-per-floor windows)
    (q/translate (* windows-per-floor 10) 0)
    )
  (q/pop-matrix))

(defn- draw-reflections [skyscrapers w h]
  (let [pg (q/create-graphics w h)
        img (q/create-image w h :rgb)]
    (.beginDraw pg)
    (.pushMatrix pg)
    (doseq [[i {:keys [brick-color storeys windows-per-floor windows]}] skyscrapers]
      (let [muted-brick-color (int (* 0.2 brick-color))
            muted-window-color (map #(* % 0.2) [255 255 200])]
        (.noStroke pg)
        (.fill pg brick-color)
        (.rect pg 0 0 (* windows-per-floor 10) (* storeys 10))

        (.pushMatrix pg)
;        (.fill pg 255 255 200)
        (.fill pg 50 50 40)
        (dotimes [s storeys]
          (.pushMatrix pg)
          (dotimes [w windows-per-floor]
          (if (zero? (windows (+ w (* s windows-per-floor))))
              (.rect pg 0 0 5 5))
            (.translate pg 10 0))
          (.popMatrix pg)
          (.translate pg 0 10))
        (.popMatrix pg)
        )
      (.translate pg (* windows-per-floor 10) 0)
      )
    (.popMatrix pg)

    (.loadPixels pg)
    (let [pg-pixels (.pixels pg)
          img-pixels (.pixels pg)
          max-i (dec (count pg-pixels))
          a 25
          p 50]
      (dotimes [i 700000] ; This is to avoid an IndexOutOfBoundsException
        (let [x (mod i w)
              y (quot i w)
              source-i (+ x (* y w) (rand-int 10) (* a (q/sin (* q/TWO-PI (/ y (+ p (rand-int 5)))))))]
           (aset img-pixels i (aget pg-pixels source-i))
          )
        )
      (set! (.pixels img) img-pixels)
      )

    (q/image img 0 0)))

(defn- water [w h]
  (let [water-color [255 255 255]]
;  (let [water-color [0 25 30]]
    (q/no-stroke)
    (apply q/fill water-color)
    (q/rect 0 0 w h))
  )

; Blinking windows?
(defn draw []
  (let [w            (q/width)
        h            (q/height)
        sky-h        (* h 0.6)
        water-h      (- h sky-h)
        skyscrapers  (make-skyscrapers 40)]
    (q/translate 0 0)
    (sunset w sky-h)
    (moon w sky-h)
    (stars w sky-h)
    (q/translate 0 sky-h)
    (water w water-h)
    (draw-skyscrapers skyscrapers)
    (draw-reflections skyscrapers w water-h)
    (q/save "cityscape.png")
    ))

(q/defsketch cityscape
  :title "cityscape"
  :setup setup
  :renderer :p3d
  :draw draw
  :size [1920 1080])

// 位置图标弹跳动画类
export class IconJumpAnimate {
    constructor() {
        // 动画相关参数
        this.gravity = 0.2 // 重力参数，影响跳跃速度
        this.buffer = 0 //
        this.startSpeed = -2 // 初速度
        this.currentSpeed = -2 // 瞬时速度
        this.top = 400 // 弹跳高度
        this.maxTop = 400 // 最高高度
        this.w = 30
        this.h = 30
    }

    // canvas会调用draw方法绘制该动画，传入context参数
    draw(context) {
        if (!context.activeInterestSpot || !context.hightLightIcon) return
        const {spot, name} = context.activeInterestSpot
        if (!spot) return
        let [x, y,angle] = spot
        x = x * context.scale + context.ax
        y = y * context.scale + context.ay
        this.currentSpeed += this.gravity
        this.top += this.currentSpeed
        if (this.top > this.maxTop) {
            this.top -= this.currentSpeed
        }
        context.ctx.strokeStyle = 'green'
        context.ctx.setLineDash([])
        context.ctx.beginPath()
        context.ctx.ellipse(
            x,
            y,  // 开始的 x,y 坐标位置。
            4,
            2,
            0,
            0,
            Math.PI * 2,
            true
        )
        context.ctx.closePath()
        context.ctx.stroke() // 调用样式
        if (name) {
            context.ctx.fillStyle = 'green'
            context.ctx.fillText(name, x - this.w / 2, y + this.top - this.maxTop - this.h)
        }
        // context.ctx.rotate(((angle - 180) / 180) * Math.PI) // 偏转坐标系角度
        context.ctx.drawImage(
            context.hightLightIcon, // 规定要使用的图像、画布或视频。
            x - this.w / 2,
            y + this.top - this.maxTop - this.h,
            this.w,
            this.h // 剪裁大小x,y
        )
        if (this.top >= this.maxTop) {
            this.startSpeed += this.buffer
            this.currentSpeed = this.startSpeed
        }
    }
}

// 加载图片loading动画类
export class LoadingAnimate {
    constructor() {
        this.isShow = false
        this.base = 0 //使用base来指示最大的圆点所在的位置, 实现旋转动画的效果
    }

    show() {
        this.base = 0
        this.isShow = true
    }

    close() {
        this.isShow = false
    }

    draw(context) {
        if (!this.isShow) return
        const ctx = context.ctx
        ctx.fillStyle = 'transparent'
        ctx.fillStyle = '#cdcdcd' //定义点的颜色
        ctx.translate(context.canvas.width / 2, context.canvas.height / 3)//把坐标原点移动到画布中央
        this.base = (++this.base === 13) ? (this.base - 12) : this.base//使用this.base来指示最大的圆点所在的位置, 实现旋转动画的效果
        const angle = Math.PI / 6//画12个点, 所以每个点之间的角度是 1/6 PI
        const beginAngle = angle * this.base
        for (let i = 1; i <= 12; i++) {
            ctx.beginPath()//开始一个路径
            if (i === 1) {
                ctx.rotate(beginAngle)
            } else {
                ctx.rotate(angle)//每次调用rotate之后, 其旋转角度并不会还原, 而是接着上一次的位置
            }
            ctx.arc(0, -30, i * 0.5 + 1, 0, 2 * Math.PI, true)//绘制一个圆点
            ctx.closePath()//闭合路径
            //如果不是用beginPath和closePath, 在调用fill方法时, 会导致画布上的所有圆都重叠在一起了
            ctx.fill()//填充(使用上面最后定义的fillStyle)
        }
    }
}

// 当前位置图标动画类
export class CurrentPosAnimate {
    constructor() {
        this.size = 80 // 当前位置图标大小
        this.duration = 1000
        this.currentPosition = null
    }

    setCurrentPosition(currentPosition) {
        this.currentPosition = currentPosition
        // console.log('MapControl drawCurrentPosition >>>', this.currentPosition)
    }

    draw(context) {
        if (!this.currentPosition || !context.mapIsLoaded) {
            return
        }
        let [x, y, angle] = this.currentPosition
        if (typeof x !== 'number') {
            x = Number(x)
            x = isNaN(x) ? 0 : x
        }
        if (typeof y !== 'number') {
            y = Number(y)
            y = isNaN(y) ? 0 : y
        }
        if (typeof angle !== 'number') {
            angle = Number(angle)
            angle = isNaN(angle) ? 0 : angle
        }
        const t = (performance.now() % this.duration) / this.duration
        const radius = this.size / 2 * 0.2
        const outerRadius = this.size / 2 * 0.7 * t + radius
        const positionX = x * context.scale + context.x
        const positionY = y * context.scale + context.y
        // 旋转中心点移到绘制点
        context.ctx.translate(positionX, positionY)
        // 将画布旋转angle角度
        context.ctx.rotate((angle - 90) / 180 * Math.PI)
        // 绘制初始点
        context.ctx.moveTo(positionX, positionY)
        // 设置直线
        context.ctx.setLineDash([1, 0])
        // 绘制外圆
        context.ctx.beginPath()
        context.ctx.arc(0, 0, outerRadius, 0, Math.PI * 2)
        context.ctx.fillStyle = 'rgba(100, 100, 255,' + (1 - t) + ')'
        context.ctx.fill()
        // 绘制内圆
        context.ctx.beginPath()
        context.ctx.arc(0, 0, radius, Math.PI / 4, Math.PI * 7 / 4)
        context.ctx.fillStyle = 'rgba(100, 100, 255, 1)'
        context.ctx.strokeStyle = 'white'
        context.ctx.lineWidth = 2 + (1 - t)
        // 表示方向的角
        context.ctx.lineTo(1.6 * radius, 0)
        context.ctx.closePath()
        context.ctx.fill()
        context.ctx.stroke()
    }
}

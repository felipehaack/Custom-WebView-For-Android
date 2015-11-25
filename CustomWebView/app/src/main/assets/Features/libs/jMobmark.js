/** 
 * @fileOverview API jMobmark criada para facilitar desenvolvimento html5, com as principais chamadas de funções. 
 * @author Felipe Haack Schmitz
 * @version 1.7
 */
var jMobmark = {
    /**
     * @argument {maxWidth} int Contém a largura máxima de toda a tela;
     */
    maxWidth: 0,
    /**
     * @argument {maxHeight} int Contém a altura máxima de toda a tela;
     */
    maxHeight: 0,
    /**
     * @argument {hasWebKit} string Se o browser utiliza webkit no CSS. Vai conter nela "-webkit-", é util quando o javascript criar/modificar um container, por exemplo: $(div).css(jMobmark.hasWebKit + 'transform': 'translate3d(10px, 10px, 0px)');
     */
    hasWebKit: "",
    /**
     * @argument {idAnalytics} string Array de IDs do Google Analytics;
     */
    idAnalytics: new Array(),
    /**
     * @argument {siteAnalytics} string variavel opcional para Google Analytics;
     */
    siteAnalytics: "",
    /**
     * TouchOrClick é uma variavel que contém filhos que auxiliam na distinção de Touch (mobile) ou Mouse (desktop)
     * Com isto é possível criar funções especificas para, por exemplo, o Click do Mouse ou o Touch do Dedo
     * @argument {start} string Inicia com mousedown (botão do mouse esquerdo pressionado), se for mobile é alterado para touchstart (pressionou o dedo na tela)
     * @argument {move} string Inicia com mousemove (deslocou o mouse sobre a tela), se for mobile é alterado para touchmove (deslocou o dedo pressionando-o sobre a tela)
     * @argument {end} string Inicia com mouseup (dedo do mouse liberado), se for mobile é alterado para touchend (dedo liberado da tela)
     */
    touchOrClick: {
        start: 'mousedown',
        move: 'mousemove',
        end: 'mouseup'
    },
    /**
     * Ajusta o CONTAINER MAIN com a resolução do dispositivo
     * Com isto pode-se trabalhar com layout em pixels (px)
     * @argument {t} int largura da div main. Ex: 768
     * @argument {n} int altura da div main. Ex: 1024
     * @argument {r} string Div Main. Ex: #container
     * @argument {i} callback nome da função que será chamado ao termino da função
     */
    layoutAdjust: function(t, n, r, i) {

        var element = document.getElementById(r);

        element.style[jMobmark.hasWebKit + 'transform-origin'] = '0% 0%';

        var s = Math.min(jMobmark.maxWidth / t, jMobmark.maxHeight / n).toFixed(3);

        element.style[jMobmark.hasWebKit + 'transform'] = 'matrix(' + s + ', 0, 0, ' + s + ', ' + parseFloat(((jMobmark.maxWidth - (t * s)) / 2)).toFixed(2) + ', ' + parseFloat(((jMobmark.maxHeight - (n * s)) / 2)).toFixed(2) + ')';

        window.setTimeout(function() {

            if (document.getElementById('containerMasterAdmag') === null) {

                var container = document.createElement('div');
                container.setAttribute('id', 'containerMasterAdmag');
                container.style.width = '100%';
                container.style.height = '100%';
                container.style.overflow = 'hidden';
                container.style.position = 'absolute';

                document.body.appendChild(container);
            }

            window.setTimeout(function() {

                if (document.getElementById('containerMasterAdmag').childElementCount === 0)
                    container.appendChild(element);

                element.style.display = 'block';
                element.style.opacity = '1';

                if (typeof i !== "undefined")
                    i();

                window.setTimeout(function(){

                    $('#containerMasterAdmag').css('overflow', 'visible');

                    window.setTimeout(function(){

                        $('#containerMasterAdmag').css('overflow', 'hidden');
                    }, 10);
                }, 1500);
            }, 33);
        }, 33);
    },
    analytics: {
        send: {
            cookiePersist: '',
            url: 'http://www.google-analytics.com/__utm.gif?utmt=event',
            cookie: function() {

                var c = function(e, j) {

                    return e + Math.floor(Math.random() * (j - e));
                };

                this.cookiePersist = '__utma%3D' + c(10000000, 99999999) + '.' + c(1000000000, 2147483647) + '.' + new Date().getTime() + '.' + new Date().getTime() + '.' + new Date().getTime() + '.2%3B%2B__utmb%3D' + c(10000000, 99999999) + '%3B%2B__utmc%3D' + c(10000000, 99999999) + '%3B%2B__utmz%3D' + c(10000000, 99999999) + '.' + new Date().getTime() + '.2.2.utmccn%3D(referral)%7Cutmcsr%3D' + window.location.pathname + '%7Cutmcct%3D' + window.location.pathname + '%7Cutmcmd%3Dreferral%3B%2B__utmv%3D' + c(10000000, 99999999) + '.-%3B';

                return this.cookiePersist;
            },
            generate: function(idAnalytics, website, category, action, label, value) {

                var track = {
                    utmac: idAnalytics,
                    utme: '5(' + category + '*' + action + '*' + label + ')(' + value + ')',
                    utmhn: website,
                    utmfl: '-',
                    utmje: '1',
                    utmwv: '1.3',
                    utmsc: '24-bit',
                    utmn: new Date().getTime(),
                    utmr: window.location.href,
                    utmp: window.location.pathname,
                    utmsr: jMobmark.maxWidth + 'x' + jMobmark.maxHeight,
                    utmdt: typeof document.title !== 'undefined' ? document.title : 'notfound',
                    utmul: typeof navigator.language !== 'undefined' ? navigator.language : 'pt-BR',
                    utmcc: this.cookiePersist === '' ? this.cookie() : this.cookiePersist
                };

                var result = '';
                for (var key in track)
                    result += '&' + key + '=' + track[key];

                return this.url + result;
            },
            init: function(idAnalytics, website, category, action, label, value) {

                var image = new Image();

                if (typeof value === 'undefined')
                    value = 1;

                image.src = this.generate(idAnalytics, website, category, action, label, value);
            }
        },
        report: {
            validate: function() {

                if (jMobmark.idAnalytics.constructor === Array && jMobmark.siteAnalytics !== '')
                    if (jMobmark.idAnalytics.length > 0)
                        return true;

                return false;
            },
            /**
             * Evento default para reportar os views gerados pelo AD.
             * @argument {company} string Empresa que esta vinculado ao anuncio.
             * @argument {campaign} string O nome da campanha ou produto.
             * @argument {category} string A categoria que faz parte da campanha. Ex: mulher, homem, negocios, etc..
             */
            view: function(company, campaign, category) {

                if (this.validate())
                    for (var i = 0; i < jMobmark.idAnalytics.length; ++i)
                        jMobmark.analytics.send.init(jMobmark.idAnalytics[i], jMobmark.siteAnalytics, 'company/' + company + '/campaign/' + campaign + '/category/' + category, 'category_view', 'views');
            },
            /**
             * Evento default para reportar eventos de click
             * @argument {company} string Empresa que esta vinculado ao anuncio.
             * @argument {campaign} string O nome da campanha ou produto.
             * @argument {category} string A categoria que faz parte da campanha. Ex: mulher, homem, negocios, etc..
             */
            click: function(company, campaign, category) {

                if (this.validate())
                    for (var i = 0; i < jMobmark.idAnalytics.length; ++i)
                        jMobmark.analytics.send.init(jMobmark.idAnalytics[i], jMobmark.siteAnalytics, 'company/' + company + '/campaign/' + campaign + '/category/' + category, 'category_click', 'clicks');
            },
            /**
             * Evento para contabilizar uma ação generica
             * @argument {company} string Empresa que esta vinculado ao anuncio.
             * @argument {campaign} string O nome da campanha ou produto.
             * @argument {category} string A categoria que faz parte da campanha. Ex: mulher, homem, negocios, etc..
             * @argument {action} string O tipo de ação. Ex: video, audio, shake, etc.
             * @argument {label} string O tipo de ação que vai conter a quantidade de visualização da ação.
             */
            generic: function(company, campaign, category, action, label) {

                if (this.validate())
                    for (var i = 0; i < jMobmark.idAnalytics.length; ++i)
                        jMobmark.analytics.send.init(jMobmark.idAnalytics[i], jMobmark.siteAnalytics, 'company/' + company + '/campaign/' + campaign + '/category/' + category, 'category_' + action, label);
            }
        }
    },
    adserveImpressao: function(a) {

        var image = new Image();

        image.src = a;
    },
    touch: {
        event: {
            pageX: '',
            pageY: '',
            pageEndX: '',
            pageEndY: ''
        }
    },
    is: {
        /**
         * Returna TRUE se for IOS
         */
        ios: function() {

            return (navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false);
        },
        /**
         * Returna TRUE se for Android
         */
        android: function() {

            return (navigator.userAgent.match(/(Android|android)/g) ? true : false);
        },
        /**
         * Returna TRUE se for desktop
         */
        desktop: function() {

            return jMobmark.touchOrClick.start === 'mousedown' ? true : false;
        },
        /**
         * Returna TRUE se o dispositivo tem tela retina
         */
        retina: function() {

            var mediaQuery = "(-webkit-min-device-pixel-ratio: 1.5),\
                      (min--moz-device-pixel-ratio: 1.5),\
                      (-o-min-device-pixel-ratio: 3/2),\
                      (min-resolution: 1.5dppx)";

            if (window.devicePixelRatio > 1)
                return true;

            if (window.matchMedia && window.matchMedia(mediaQuery).matches)
                return true;

            return false;
        }
    },
    set: {
        widthAndHeight: function() {

            jMobmark.maxWidth = jMobmark.screen.width();
            jMobmark.maxHeight = jMobmark.screen.height();
        },
        touchOrClick: function() {

            var t = "ontouchstart" in document.documentElement;
            if (t) {
                jMobmark.touchOrClick.start = "touchstart";
                jMobmark.touchOrClick.move = "touchmove";
                jMobmark.touchOrClick.end = "touchend"
            }
        },
        webKit: function() {

            var t = 'WebkitAppearance' in document.documentElement.style;
            if (t) {
                jMobmark.hasWebKit = "-webkit-"
            }
        },
        pageEndXY: function() {

            if (jMobmark.touchOrClick.start === 'touchstart') {

                jMobmark.touch.event.pageEndX = function(e) {

                    return e.originalEvent.changedTouches[0].pageX;
                };

                jMobmark.touch.event.pageEndY = function(e) {

                    return e.originalEvent.changedTouches[0].pageY;
                };
            } else {

                jMobmark.touch.event.pageEndX = function(e) {

                    return e.pageX;
                };

                jMobmark.touch.event.pageEndY = function(e) {

                    return e.pageY;
                };
            }
        },
        pageXY: function() {

            if (jMobmark.touchOrClick.start === 'touchstart') {

                jMobmark.touch.event.pageX = function(e, p) {

                    return e.originalEvent.touches.length > p ? e.originalEvent.touches[p].pageX : -1;
                };

                jMobmark.touch.event.pageY = function(e, p) {

                    return e.originalEvent.touches.length > p ? e.originalEvent.touches[p].pageY : -1;
                };
            } else {

                jMobmark.touch.event.pageX = function(e, p) {

                    return p === 0 ? e.pageX : -1;
                };

                jMobmark.touch.event.pageY = function(e, p) {

                    return p === 0 ? e.pageY : -1;
                };
            }
        }
    },
    screen: {
        /**
         * Return Window Width or set Window Width
         * @argument {number} width
         * @returns {Window.innerWidth|Window.outerWidth|0}
         */
        width: function() {

            return $(window).width();
        },
        /**
         * Return Window Height Or set Window Height
         * @argument {number} height
         * @returns {Window.innerHeight|Window.outerHeight|0}
         */
        height: function() {

            return $(window).height();
        },
        rotate: {
            callback: '',
            resize: function() {

                if (typeof jMobmark.screen.rotate.callback === 'function') {

                    jMobmark.screen.rotate.callback();
                } else
                    window.location.href = 'index.html';
            },
            build: function() {

                window.setInterval(function() {
                    
                    if (jMobmark.maxWidth !== $(window).width()) {

                        window.setTimeout(function() {

                            jMobmark.set.widthAndHeight();

                            window.setTimeout(function() {

                                jMobmark.screen.rotate.resize();
                            }, 250);
                        }, 33);
                    }
                }, 500);
            },
            init: function(obj) {

                if (typeof obj === 'function')
                    jMobmark.screen.rotate.callback = obj;

                jMobmark.screen.rotate.build();
            }
        }
    },
    init: function() {

        jMobmark.set.widthAndHeight();
        jMobmark.set.touchOrClick();
        jMobmark.set.webKit();
        jMobmark.set.pageXY();
        jMobmark.set.pageEndXY();
    }
};

jMobmark.init();
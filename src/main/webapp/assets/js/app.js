/*
 * ReClaim - small progressive enhancements.
 * Every page works without JavaScript; these only improve the experience.
 * All server-side validation still happens in the Java service layer.
 */
(function () {
    'use strict';

    // Mobile navigation toggle
    var toggle = document.querySelector('[data-nav-toggle]');
    var nav = document.querySelector('[data-nav]');
    if (toggle && nav) {
        toggle.addEventListener('click', function () {
            var open = nav.classList.toggle('is-open');
            toggle.setAttribute('aria-expanded', open ? 'true' : 'false');
        });
    }

    // Dismissible flash messages
    document.querySelectorAll('[data-dismiss]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var alert = btn.closest('.alert');
            if (alert) alert.remove();
        });
    });

    // Show / hide password
    document.querySelectorAll('[data-toggle-password]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var input = document.getElementById(btn.getAttribute('data-toggle-password'));
            if (!input) return;
            var show = input.type === 'password';
            input.type = show ? 'text' : 'password';
            btn.setAttribute('aria-label', show ? 'Hide password' : 'Show password');
        });
    });

    // Password strength meter
    document.querySelectorAll('[data-strength]').forEach(function (input) {
        var meter = input.closest('.field').querySelector('[data-strength-meter] span');
        if (!meter) return;
        input.addEventListener('input', function () {
            var v = input.value, score = 0;
            if (v.length >= 8) score++;
            if (v.length >= 12) score++;
            if (/[a-zA-Z]/.test(v) && /\d/.test(v)) score++;
            if (/[^a-zA-Z0-9]/.test(v)) score++;
            var colours = ['#e11d48', '#f59e0b', '#eab308', '#10b981', '#059669'];
            meter.style.width = (v.length ? Math.max(score, 1) * 25 : 0) + '%';
            meter.style.background = colours[score];
        });
    });

    // Confirm password must match
    document.querySelectorAll('[data-match]').forEach(function (input) {
        var other = document.getElementById(input.getAttribute('data-match'));
        function check() {
            input.setCustomValidity(input.value && other && input.value !== other.value ? 'Passwords do not match.' : '');
        }
        input.addEventListener('input', check);
        if (other) other.addEventListener('input', check);
    });

    // Browser-side validation: show the native hints, focus the first invalid field
    document.querySelectorAll('form[data-validate]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                form.reportValidity();
                return;
            }
            var submit = form.querySelector('button[type=submit]');
            if (submit) {
                // Prevent double submission
                setTimeout(function () { submit.disabled = true; }, 0);
            }
        });
    });

    // Re-enable submit buttons when the page is restored with the Back button
    window.addEventListener('pageshow', function () {
        document.querySelectorAll('form[data-validate] button[type=submit]').forEach(function (b) { b.disabled = false; });
    });

    // Character counter for textareas
    document.querySelectorAll('[data-counter]').forEach(function (area) {
        var counter = document.querySelector('[data-counter-for="' + area.id + '"]');
        if (!counter) return;
        var max = parseInt(area.getAttribute('maxlength'), 10) || 1000;
        function update() {
            counter.textContent = area.value.length + ' / ' + max;
            counter.classList.toggle('is-near', area.value.length > max * 0.9);
        }
        area.addEventListener('input', update);
        update();
    });

    // Report form: switch LOST / FOUND theme and texts without reloading
    var reportForm = document.querySelector('[data-report-form]');
    if (reportForm) {
        var applyType = function (type) {
            var lost = type === 'LOST';
            reportForm.classList.toggle('theme-lost', lost);
            reportForm.classList.toggle('theme-found', !lost);
            document.querySelectorAll('[data-lost][data-found]').forEach(function (el) {
                el.textContent = el.getAttribute(lost ? 'data-lost' : 'data-found');
            });
            var title = document.querySelector('[data-type-title]');
            if (title) document.title = title.textContent + ' · ReClaim';
        };
        reportForm.querySelectorAll('input[name=type]').forEach(function (radio) {
            radio.addEventListener('change', function () { applyType(radio.value); });
        });
    }

    // Image picker: size/type check and live preview
    document.querySelectorAll('[data-image-input]').forEach(function (input) {
        var zone = input.closest('[data-dropzone]');
        var preview = zone.querySelector('.dropzone__preview');
        var error = zone.parentElement.querySelector('[data-image-error]');
        var maxBytes = parseInt(input.getAttribute('data-max-bytes'), 10);
        var allowed = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

        function fail(message) {
            input.value = '';
            zone.classList.remove('has-file');
            preview.hidden = true;
            error.textContent = message;
            error.hidden = false;
        }

        input.addEventListener('change', function () {
            error.hidden = true;
            var file = input.files && input.files[0];
            if (!file) { zone.classList.remove('has-file'); preview.hidden = true; return; }
            if (allowed.indexOf(file.type) === -1) return fail('Only JPG, PNG, GIF or WEBP images are allowed.');
            if (file.size > maxBytes) return fail('This photo is ' + (file.size / 1048576).toFixed(1) + ' MB. Please choose one under 5 MB.');
            var reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.hidden = false;
                zone.classList.add('has-file');
            };
            reader.readAsDataURL(file);
        });
        ['dragenter', 'dragover'].forEach(function (evt) {
            zone.addEventListener(evt, function () { zone.classList.add('is-dragover'); });
        });
        ['dragleave', 'drop'].forEach(function (evt) {
            zone.addEventListener(evt, function () { zone.classList.remove('is-dragover'); });
        });
    });

    // Confirmation before irreversible-looking actions
    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!window.confirm(form.getAttribute('data-confirm'))) e.preventDefault();
        });
    });

    // Browse filters: apply immediately when a select or pill changes
    document.querySelectorAll('[data-autosubmit]').forEach(function (el) {
        el.addEventListener('change', function () { el.form.submit(); });
    });
})();

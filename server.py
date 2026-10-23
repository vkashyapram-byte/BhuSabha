"""
BhuSabha Localhost Backend Server
Runs on port 8000 (accessible from Android Studio Emulator via http://10.0.2.2:8000)
"""
import http.server
import socketserver
import json
import urllib.parse

PORT = 8000

# In-memory reactive state
PARCELS = [
    {
        "ulpin": "MH-PUN-2024-009182",
        "survey_number": "142/2A",
        "owner_name": "Rameshwar Prasad Sharma",
        "village": "Wagholi",
        "taluka": "Haveli",
        "district": "Pune",
        "state": "Maharashtra",
        "area_hectares": 2.45,
        "land_type": "Agricultural (Irrigated)",
        "health_score": 94,
        "encumbrance_status": "Clean Title - No active mortgages or stays",
        "annual_tax": 4250.0,
        "tax_paid": True,
        "last_updated": "14-Aug-2026"
    },
    {
        "ulpin": "MH-PUN-2024-004312",
        "survey_number": "89/1B",
        "owner_name": "Rameshwar Prasad Sharma",
        "village": "Lohegaon",
        "taluka": "Haveli",
        "district": "Pune",
        "state": "Maharashtra",
        "area_hectares": 0.85,
        "land_type": "Residential / Gaothan",
        "health_score": 88,
        "encumbrance_status": "NOC granted by Local Gram Panchayat",
        "annual_tax": 8900.0,
        "tax_paid": True,
        "last_updated": "02-Jul-2026"
    },
    {
        "ulpin": "MH-PUN-2024-007741",
        "survey_number": "204/5",
        "owner_name": "Kailash Nath",
        "village": "Kharadi",
        "taluka": "Haveli",
        "district": "Pune",
        "state": "Maharashtra",
        "area_hectares": 1.20,
        "land_type": "Commercial Zone",
        "health_score": 58,
        "encumbrance_status": "Active Boundary Demarcation Dispute",
        "annual_tax": 14200.0,
        "tax_paid": False,
        "last_updated": "28-Sep-2026"
    }
]

MUTATIONS = [
    {
        "id": "MUT-2026-9041",
        "ulpin": "MH-PUN-2024-009182",
        "applicant_name": "Ananya Sharma",
        "current_owner": "Rameshwar Prasad Sharma",
        "proposed_owner": "Ananya Sharma",
        "deed_type": "Registered Sale Deed #4019",
        "submission_date": "20-Oct-2026",
        "status": "PENDING_INSPECTION",
        "risk_score": 12,
        "discrepancy_note": None
    },
    {
        "id": "MUT-2026-8820",
        "ulpin": "MH-PUN-2024-007741",
        "applicant_name": "Rajesh Nath",
        "current_owner": "Kailash Nath",
        "proposed_owner": "Rajesh Nath",
        "deed_type": "Family Partition Deed",
        "submission_date": "16-Oct-2026",
        "status": "DISCREPANCY_FLAGGED",
        "risk_score": 78,
        "discrepancy_note": "Declared cadastral area (1.35 Ha) exceeds revenue map survey record (1.20 Ha)"
    }
]

class BhuSabhaApiHandler(http.server.SimpleHTTPRequestHandler):
    def _send_cors_headers(self):
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE")
        self.send_header("Access-Control-Allow-Headers", "Content-Type, Authorization")

    def do_OPTIONS(self):
        self.send_response(200)
        self._send_cors_headers()
        self.end_headers()

    def do_GET(self):
        url = urllib.parse.urlparse(self.path)
        path = url.path

        if path == "/health" or path == "/api/v1/health":
            self._send_json({"status": "healthy", "service": "BhuSabha Core Gateway", "offline_ready": True})
            return

        if path == "/api/v1/parcels":
            query = urllib.parse.parse_qs(url.query).get("q", [""])[0].lower()
            results = PARCELS
            if query:
                results = [p for p in PARCELS if query in p["ulpin"].lower() or query in p["owner_name"].lower()]
            self._send_json({"parcels": results, "total": len(results)})
            return

        if path == "/api/v1/mutations":
            self._send_json({"mutations": MUTATIONS, "total": len(MUTATIONS)})
            return

        # Fallback to file serving (e.g. code.html)
        return super().do_GET()

    def do_POST(self):
        url = urllib.parse.urlparse(self.path)
        path = url.path
        length = int(self.headers.get("Content-Length", 0))
        body = self.rfile.read(length).decode("utf-8") if length > 0 else "{}"
        try:
            payload = json.loads(body)
        except Exception:
            payload = {}

        if path == "/api/v1/auth/citizen":
            otp = payload.get("otp", "")
            if len(otp) == 6:
                self._send_json({
                    "success": True,
                    "token": "jwt_bhusabha_citizen_982341094892",
                    "user": {
                        "name": "Rameshwar Prasad Sharma",
                        "aadhaar": "XXXX-XXXX-4892",
                        "role": "citizen"
                    }
                })
            else:
                self._send_json({"success": False, "error": "Invalid 6-digit OTP"}, status=400)
            return

        if path == "/api/v1/auth/official":
            officer_id = payload.get("employee_id", "")
            if officer_id:
                self._send_json({
                    "success": True,
                    "token": "jwt_bhusabha_official_" + officer_id,
                    "user": {
                        "name": "Suresh Deshmukh",
                        "designation": payload.get("designation", "Tahsildar"),
                        "jurisdiction": "Haveli Sub-division, Pune",
                        "role": "official"
                    }
                })
            else:
                self._send_json({"success": False, "error": "Missing employee ID"}, status=400)
            return

        if path.startswith("/api/v1/mutations/") and path.endswith("/approve"):
            app_id = path.split("/")[4]
            for m in MUTATIONS:
                if m["id"] == app_id:
                    m["status"] = "APPROVED"
                    for p in PARCELS:
                        if p["ulpin"] == m["ulpin"]:
                            p["owner_name"] = m["proposed_owner"]
                            p["last_updated"] = "Today"
                    self._send_json({"success": True, "mutation": m})
                    return
            self._send_json({"success": False, "error": "Not found"}, status=404)
            return

        self._send_json({"error": "Unknown endpoint"}, status=404)

    def _send_json(self, data, status=200):
        response_bytes = json.dumps(data, indent=2).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(response_bytes)))
        self._send_cors_headers()
        self.end_headers()
        self.wfile.write(response_bytes)

if __name__ == "__main__":
    print(f"Starting BhuSabha Localhost Server on http://localhost:{PORT}")
    print("Android Emulator alias: http://10.0.2.2:8000")
    with socketserver.TCPServer(("", PORT), BhuSabhaApiHandler) as httpd:
        httpd.serve_forever()

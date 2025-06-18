#!/bin/bash

# Simple security scanner for hardcoded secrets
# Outputs findings in JSON format

OUTPUT_FILE="target/security-report/security-findings.json"
mkdir -p target/security-report

echo "🔍 Scanning for hardcoded secrets..."

# Create temporary file for collecting findings
TEMP_FILE=$(mktemp)

# Scan properties files and collect findings
echo "📄 Scanning properties files..."
find . -name "*.properties" -not -path "./target/*" -exec grep -Hn "token\|password\|secret\|key" {} \; > "$TEMP_FILE"

# Start JSON array
echo "[" > "$OUTPUT_FILE"

count=0

# Process findings
while IFS=: read -r file line_num line_content; do
    if [ -n "$file" ] && [ -n "$line_num" ] && [ -n "$line_content" ]; then
        # Skip if value is only an environment variable reference (e.g., ${APP_TOKEN})
        if [[ "$line_content" =~ =\$\{[A-Za-z0-9_]+\}$ ]]; then
            continue
        fi
        # Add comma if not first
        if [ $count -gt 0 ]; then
            echo "," >> "$OUTPUT_FILE"
        fi
        
        # Clean content for JSON (escape quotes)
        clean_content=$(echo "$line_content" | sed 's/"/\\"/g')
        secret=$(echo "$line_content" | grep -oE '[=:]\s*[a-zA-Z0-9\-_]{8,}' | sed 's/[=:]\s*//' | head -1)
        
        # Write JSON object
        echo "  {" >> "$OUTPUT_FILE"
        echo "    \"file\": \"$file\"," >> "$OUTPUT_FILE"
        echo "    \"line\": $line_num," >> "$OUTPUT_FILE" 
        echo "    \"description\": \"Hardcoded token in properties file\"," >> "$OUTPUT_FILE"
        echo "    \"match\": \"$clean_content\"," >> "$OUTPUT_FILE"
        echo "    \"secret\": \"$secret\"," >> "$OUTPUT_FILE"
        echo "    \"severity\": \"HIGH\"" >> "$OUTPUT_FILE"
        echo -n "  }" >> "$OUTPUT_FILE"
        
        count=$((count + 1))
    fi
done < "$TEMP_FILE"

# End JSON array
echo "" >> "$OUTPUT_FILE"
echo "]" >> "$OUTPUT_FILE"

# Clean up
rm "$TEMP_FILE"

echo ""
echo "✅ Security scan completed!"
echo "📊 Found $count potential security issues"
echo "📋 Report saved to: $OUTPUT_FILE"

if [ $count -gt 0 ]; then
    echo ""
    echo "🚨 Security Issues Found:"
    if command -v jq &> /dev/null; then
        jq -r '.[] | "  - \(.file):\(.line) - \(.description)"' "$OUTPUT_FILE"
    else
        echo "  Install jq for pretty output: sudo apt install jq"
        grep '"file"' "$OUTPUT_FILE" | sed 's/.*"file": "\([^"]*\)".*/  - \1/'
    fi
fi

echo ""
echo "💡 View full report: cat $OUTPUT_FILE" 